package com.example.amtraker.api

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ApiFunctions {
    fun TrainApiCall(): Map<String, List<Train>> {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api-v3.amtraker.com/v3/trains")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val responseBody = response.body?.string()
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
            val myObject = json.decodeFromString<Map<String, List<Train>>>(responseBody.toString())
            return myObject
        }
    }

    fun StationApiCall(): Map<String, StationMeta> {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api-v3.amtraker.com/v3/stations")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val responseBody = response.body?.string()
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
            val myObject = json.decodeFromString<Map<String, StationMeta>>(responseBody.toString())
            return myObject
        }
    }
}