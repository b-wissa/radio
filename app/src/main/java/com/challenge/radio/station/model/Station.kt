package com.challenge.radio.station.model

data class Station(
    val id: String,
    val name: String,
    val city: String?,
    val genres: List<String>,
    val logo: String?,
)
