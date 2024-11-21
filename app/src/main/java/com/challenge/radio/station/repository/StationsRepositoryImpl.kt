package com.challenge.radio.station.repository

import com.challenge.radio.station.api.StationApi
import com.challenge.radio.station.model.Station

class StationsRepositoryImpl(
    private val stationApi: StationApi,
) : StationsRepository {
    override suspend fun loadTopStations(count: Int): Result<List<Station>> =
        runCatching {
            stationApi.listStationsBySystemName(
                systemName = StationApi.SystemName.STATIONS_TOP,
                count = count,
            )
        }.map { response ->
            response.playables.map {
                it.toStation()
            }
        }

    override suspend fun getStationsDetailsById(ids: List<String>): Result<List<Station>> =
        kotlin.runCatching {
            stationApi.getStationDetailsByIds(ids = ids).map { apiStationDetail ->
                apiStationDetail.toStation()
            }
        }
}
