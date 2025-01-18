package com.example.amtraker.api
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int

@Serializable
data class StationMeta(
    val name: String? = "",
    val code: String? = "",
    val tz: String? = "",
    val lat: Double,
    val lon: Double,
    val hasAddress: Boolean,
    val address1: String,
    val address2: String,
    val city: String,
    val state: String,
    @Serializable(with = ZipSerializer::class)
    val zip: String,
    val trains: List<String>
)

object ZipSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("zip", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        val input = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (input) {
            is JsonPrimitive -> if (input.isString) input.content else input.int.toString()
            else -> "Unknown"
        }
    }
}