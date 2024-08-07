package com.example.jetpackproject.presentation.weather

import com.example.jetpackproject.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
