package com.challenge.radio.station.repository

import com.challenge.radio.station.model.Station

interface StationsRepository {
    suspend fun loadTopStations(count: Int): Result<List<Station>>
}