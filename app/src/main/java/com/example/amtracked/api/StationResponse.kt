package com.example.amtraker.api
import kotlinx.serialization.Serializable

@Serializable
data class StationResponse(
    val StationMeta: Map<String, StationMeta>
)

