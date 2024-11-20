package com.challenge.radio.station.usecase

import com.challenge.radio.station.model.Station
import com.challenge.radio.station.repository.StationsRepository

class GetTopStationsUseCase(
    private val stationsRepository: StationsRepository,
) {
    suspend operator fun invoke(count: Int = DEFAULT_TOP_COUNT): Result<List<Station>> = stationsRepository.loadTopStations(count = count)

    companion object {
        const val DEFAULT_TOP_COUNT = 100
    }
}
