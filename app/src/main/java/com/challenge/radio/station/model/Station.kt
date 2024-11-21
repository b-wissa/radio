package com.challenge.radio.station.model

data class Station(
    val id: String,
    val name: String,
    val city: String?,
    val country: String?,
    val genres: List<String>,
    val topics: List<String>,
    val description: String?,
    val logo: String?,
)
