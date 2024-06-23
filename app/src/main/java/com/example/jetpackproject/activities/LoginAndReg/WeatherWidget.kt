package com.example.jetpackproject.activities.LoginAndReg

import android.Manifest
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.Alignment
import androidx.glance.Button
import androidx.glance.GlanceModifier
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

import androidx.glance.GlanceId
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.size
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.jetpackproject.R
import com.example.jetpackproject.activities.greetings.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object WeatherWidget : GlanceAppWidget() {
    private val apiKey = "8c5998f18ccf9118f1d56e8db5ea65e0"
    private var iconBitmap by mutableStateOf<Bitmap?>(null)

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sharedPref = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        val isUserLoggedIn = sharedPref.getBoolean("isUserLoggedIn", false)

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "weatherWidgetWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequest.Builder(
                WeatherWidgetWorker::class.java,
                60.minutes.toJavaDuration()
            ).build()
        )

        provideContent {
            val weatherViewModel = WeatherViewModel()
            weatherViewModel.fetchWeatherByCurrentLocation(context, apiKey)

            val weatherData by remember { weatherViewModel.weatherData }
            val lastUpdateTime by rememberUpdatedState(newValue = getCurrentTime())

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color.Gray),
                verticalAlignment = Alignment.Vertical.CenterVertically,
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
            ) {
                if (isUserLoggedIn && (checkLocationPermissions(context))) {
                    weatherData?.let { data ->
                        val temperatureCelsius = (data.main.temp - 273).toInt()
                        val feelsLikeCelsius = (data.main.feels_like - 273).toInt()
                        Text(
                            text = "Темп: $temperatureCelsius°C\nОщущается как: $feelsLikeCelsius°C\nГород: ${data.name}\nПоследнее обновление: $lastUpdateTime",
                            style = TextStyle(fontSize = 20.sp)
                        )
                        LaunchedEffect(key1 = data.weather[0].icon) {
                            iconBitmap = loadImageAsBitmap("https://openweathermap.org/img/w/${data.weather[0].icon}.png")
                        }
                        Image(
                            modifier = GlanceModifier.size(40.dp),
                            provider = if (iconBitmap != null) {
                                ImageProvider(iconBitmap!!)
                            } else {
                                ImageProvider(R.drawable.ic_launcher_foreground)
                            },
                            contentDescription = "Weather icon")

                        Button(
                            text = "Обновить",
                            onClick = actionRunCallback(RefreshWeather::class.java)
                        )
                    } ?: run {
                        Text(
                            text = "Загрузка погоды...",
                            style = TextStyle(
                                fontSize = 20.sp
                            )
                        )
                    }
                } else if(!isUserLoggedIn) {
                    Text(
                        text = "Необходимо авторизоваться",
                        style = TextStyle(
                            fontSize = 20.sp
                        )
                    )
                } else if(!(checkLocationPermissions(context))) {
                    Text(
                        text = "Нужно разрешение на геолокацию",
                        style = TextStyle(
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
    }

    private suspend fun loadImageAsBitmap(url: String): Bitmap? {
        return try {
            withContext(Dispatchers.IO) {
                BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }
}


class Simple : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WeatherWidget
}

class RefreshWeather : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WeatherWidget.update(context, glanceId)
    }
}

class WeatherWidgetWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        WeatherWidget.apply {
            updateAll(applicationContext)
        }
        return Result.success()
    }
}

private fun checkLocationPermissions(context: Context): Boolean {
    val fineLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fineLocationPermission && coarseLocationPermission
}
