package com.example.amtraker.api
import kotlinx.serialization.Serializable

@Serializable
data class TrainResponse(
    val Train: Map<String, List<Train>>
)
