package com.challenge.radio.station.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StationsBySystemNameResponse(
    @SerialName("count") val count: Int,
    @SerialName("playables") val playables: List<Playable>,
) {
    @Serializable
    data class Playable(
        @SerialName("id") val id: String,
        @SerialName("city") val city: String?,
        @SerialName("name") val name: String?,
        @SerialName("logo300x300") val logo: String?,
        @SerialName("genres") val genres: List<String>?,
    )
}
