package com.example.jetpackproject.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeatherInput(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun getWeatherLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>
}
