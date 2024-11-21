package com.challenge.radio.station.usecase

import com.challenge.radio.station.repository.StationsRepository

class GetStationDetailsByIdUseCase(
    private val stationsRepository: StationsRepository,
) {
    suspend operator fun invoke(stationId: String) =
        stationsRepository.getStationsDetailsById(listOf(stationId)).mapCatching {
            it.first()
        }
}
