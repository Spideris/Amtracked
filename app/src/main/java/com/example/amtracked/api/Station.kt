package com.example.amtraker.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Station(
    @SerialName("name") val name: String? = this.toString(),
    @SerialName("code") val code: String? = "",
    @SerialName("tz") val tz: String? = "",
    @SerialName("bus") val bus: Boolean,
    @SerialName("schArr") val schArr: String? = "",
    @SerialName("schDep") val schDep: String? = "",
    @SerialName("arr") val arr: String? = "",
    @SerialName("dep") val dep: String? = "",
    @SerialName("arrCmnt") val arrCmnt: String? = "",
    @SerialName("depCmnt") val depCmnt: String? = "",
    @SerialName("status") val status: String? = "",
    @SerialName("platform") val platform: String? = ""
)