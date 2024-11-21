package com.challenge.radio.station.api

import com.challenge.radio.station.api.model.ApiStationDetail
import com.challenge.radio.station.api.model.StationsBySystemNameResponse
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApi {
    @GET("stations/list-by-system-name")
    suspend fun listStationsBySystemName(
        @Query("systemName") systemName: SystemName,
        @Query("count") count: Int,
    ): StationsBySystemNameResponse

    @GET("stations/details")
    suspend fun getStationDetailsByIds(
        @Query("stationIds") ids: List<String>,
    ): List<ApiStationDetail>

    @Serializable
    enum class SystemName {
        STATIONS_TOP,
    }
}
