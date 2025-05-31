package com.example.amtraker.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Train (
    @SerialName("routeName") val routeName: String?,
    @SerialName("trainNum") val trainNum: String?,
    @SerialName("trainNumRaw") val trainNumRaw: String?,
    @SerialName("trainID") val trainID: String?,
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double,
    @SerialName("trainTimely") val trainTimely: String,
    @SerialName("iconColor") val iconColor: String?,
    @SerialName("stations") val stations: List<Station>,
    @SerialName("heading") val heading: String,
    @SerialName("eventCode") val eventCode: String,
    @SerialName("eventTZ") val eventTZ: String? = ("Unspecified"),
    @SerialName("eventName") val eventName: String? = ("Unspecified"),
    @SerialName("origCode") val origCode: String,
    @SerialName("originTZ") val originTZ: String,
    @SerialName("origName") val origName: String,
    @SerialName("destCode") val destCode: String,
    @SerialName("destTZ") val destTZ: String? = ("Unspecified"),
    @SerialName("destName") val destName: String? = ("Unspecified"),
    @SerialName("trainState") val trainState: String,
    @SerialName("velocity") val velocity: Double,
    @SerialName("statusMsg") val statusMsg: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
    @SerialName("lastValTS") val lastValTS: String,
    @SerialName("objectID") val objectID: Int? = 0,
    @SerialName("provider") val provider: String,
    @SerialName("providerShort") val providerShort: String
)