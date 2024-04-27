package com.example.jetpackproject.activities.greetings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jetpackproject.data.remote.WeatherResponse
import com.google.android.gms.location.LocationServices

@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel, context: Context) {
    var city by remember { mutableStateOf("") }
    val apiKey = "8c5998f18ccf9118f1d56e8db5ea65e0"
    val PERMISSION_REQUEST_CODE = 100

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = city,
            onValueChange = {
                city = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 64.dp, end = 64.dp, top = 4.dp, bottom = 4.dp)
                .border(
                    1.dp,
                    Color(android.graphics.Color.parseColor("#7d32a8")),
                    shape = RoundedCornerShape(50)
                ),
            shape = RoundedCornerShape(50),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            placeholder = { Text(text = "Введите город") }
        )

        Button(
            onClick = {
                if (city.isNotEmpty()) {
                    weatherViewModel.getWeather(city, apiKey)
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 200.dp, height = 50.dp)
                .clip(RoundedCornerShape(50.dp))
        ) {
            Text(text = "Погода по городу")
        }

        Button(
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    weatherViewModel.fetchWeatherByCurrentLocation(context, apiKey)
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        PERMISSION_REQUEST_CODE
                    )
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .size(width = 200.dp, height = 50.dp)
                .clip(RoundedCornerShape(50.dp))
        ) {
            Text(text = "Погода по месту")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val weatherData by remember { weatherViewModel.weatherData }

        weatherData?.let { data ->
            val temperatureCelsius = (data.main.temp - 273).toInt()
            Text(
                text = "Температура: $temperatureCelsius°C",
                style = TextStyle(fontSize = 20.sp)
            )
            Text(
                text = "Скорость ветра: ${data.wind.speed} м/c",
                style = TextStyle(fontSize = 20.sp)
            )
            Text(
                text = "Давление: ${data.main.pressure}",
                style = TextStyle(fontSize = 20.sp)
            )
            GlideImage(url = "https://openweathermap.org/img/w/${data.weather[0].icon}.png")
        } ?: run {
            Box(
                modifier = Modifier
                    .size(160.dp)
            )
        }
    }
}