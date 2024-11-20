package com.challenge.radio.station.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApi {
    @GET("stations/list-by-system-name")
    suspend fun listStationsBySystemName(
        @Query("systemName") systemName: SystemName,
        @Query("count") count: Int,
    ): StationsBySystemNameResponse

    @Serializable
    enum class SystemName {
        STATIONS_TOP,
    }
}

@Serializable
data class StationsBySystemNameResponse(
    @SerialName("count") val count: Int,
    @SerialName("playables") val playables: List<Playable>,
) {
    @Serializable
    data class Playable(
        @SerialName("city") val city: String?,
        @SerialName("name") val name: String?,
        @SerialName("logo300x300") val logo: String?,
    )
}
