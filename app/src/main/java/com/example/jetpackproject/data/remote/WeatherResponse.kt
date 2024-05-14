package com.example.jetpackproject.data.remote

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String,
    val sys: Sys
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int
)

data class Weather(
    val icon: String
)

data class Wind(
    val speed: Double
)

data class Sys(
    val country: String
)