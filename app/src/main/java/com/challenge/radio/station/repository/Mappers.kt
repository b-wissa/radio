package com.challenge.radio.station.repository

import com.challenge.radio.station.api.model.StationsBySystemNameResponse
import com.challenge.radio.station.model.Station

fun StationsBySystemNameResponse.Playable.toStation() =
    Station(
        id = id,
        name = name.orEmpty(),
        city = city.orEmpty(),
        genres = genres ?: emptyList(),
        logo = logo,
    )
