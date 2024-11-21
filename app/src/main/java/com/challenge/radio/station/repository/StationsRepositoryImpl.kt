package com.challenge.radio.station.repository

import com.challenge.radio.station.api.StationApi
import com.challenge.radio.station.model.Station
// TODO [Storage]
//  For storage would use a Room database created parallel to the "api" package.
//  and inject stationsDao here, can have one table for Stations
//  Since the repository is the source of stations, when adding a database this file would be the only file to be modified
//  - in addition to the database creation /DAO code.

class StationsRepositoryImpl(
    private val stationApi: StationApi,
) : StationsRepository {
    override suspend fun loadTopStations(count: Int): Result<List<Station>> =
        runCatching {
            // TODO [Storage]
            //  Depending on the use case, if we prefer to load first from storage if existing
            //  and then do the network fetching, or use storage as backup in case the api call fails
            //  the api call would be called before or after loading stations from storage
            stationApi.listStationsBySystemName(
                systemName = StationApi.SystemName.STATIONS_TOP,
                count = count,
            )
        }.map { response ->
            // TODO [Storage]
            //  as a side effect would update the storage here with the new values
            response.playables.map {
                it.toStation()
            }
        }

    override suspend fun getStationsDetailsById(ids: List<String>): Result<List<Station>> =
        kotlin.runCatching {
            // TODO [Storage]
            //  update records with the values returned from the details api fetching
            stationApi.getStationDetailsByIds(ids = ids).map { apiStationDetail ->
                apiStationDetail.toStation()
            }
        }
}
