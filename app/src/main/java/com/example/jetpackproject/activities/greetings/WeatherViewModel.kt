package com.example.jetpackproject.activities.greetings

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackproject.data.remote.WeatherResponse
import com.example.jetpackproject.data.remote.WeatherService
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherViewModel : ViewModel() {
    private val weatherService = provideWeatherService()

    private val _weatherData = mutableStateOf<WeatherResponse?>(null) //Изменяемое состояние
    val weatherData: State<WeatherResponse?> = _weatherData //Неизменяемое состояние

    fun getWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            handleWeatherResponse(weatherService.getWeatherInput(city, apiKey))
        }
    }

    fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            handleWeatherResponse(weatherService.getWeatherLocation(latitude, longitude, apiKey))
        }
    }

    private fun handleWeatherResponse(response: Response<WeatherResponse>) {
        if (response.isSuccessful) {
            _weatherData.value = response.body()
        } else {
            Log.e("WeatherViewModel", "Failed to get weather data: ${response.errorBody()}")
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchWeatherByCurrentLocation(context: Context, apiKey: String) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val lat = location.latitude
                    val lon = location.longitude
                    getWeatherByLocation(lat, lon, apiKey)
                }
            }
            .addOnFailureListener { e ->
                Log.e("WeatherScreen", "Error getting location: ${e.message}")
            }
    }

    private fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherService::class.java)
    }
}