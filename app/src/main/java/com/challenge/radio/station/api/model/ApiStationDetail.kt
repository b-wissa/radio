package com.challenge.radio.station.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiStationDetail(
    @SerialName("id") val id: String,
    @SerialName("city") val city: String?,
    @SerialName("country") val country: String?,
    @SerialName("name") val name: String?,
    @SerialName("logo300x300") val logo: String?,
    @SerialName("genres") val genres: List<String>?,
    @SerialName("topics") val topics: List<String>?,
    @SerialName("description") val description: String?,
    @SerialName("shortDescription") val shortDescription: String?,
)
