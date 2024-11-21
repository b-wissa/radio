package com.challenge.radio.station.repository

import com.challenge.radio.station.api.model.ApiStationDetail
import com.challenge.radio.station.api.model.StationsBySystemNameResponse
import com.challenge.radio.station.model.Station

fun StationsBySystemNameResponse.Playable.toStation() =
    Station(
        id = id,
        name = name.orEmpty(),
        city = city.orEmpty(),
        genres = genres ?: emptyList(),
        logo = logo,
        country = null,
        topics = emptyList(),
        description = null,
    )

fun ApiStationDetail.toStation() =
    Station(
        id = id,
        name = name.orEmpty(),
        city = city,
        country = country,
        genres = genres ?: emptyList(),
        topics = topics ?: emptyList(),
        description = description ?: shortDescription,
        logo = logo,
    )
