package com.example.jetpackproject.presentation

//object WeatherWidget : GlanceAppWidget() {
//    private val apiKey = "8c5998f18ccf9118f1d56e8db5ea65e0"
//    private var iconBitmap by mutableStateOf<Bitmap?>(null)
//
//    override suspend fun provideGlance(context: Context, id: GlanceId) {
//        val sharedPref = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
//        val isUserLoggedIn = sharedPref.getBoolean("isUserLoggedIn", false)
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            "weatherWidgetWorker",
//            ExistingPeriodicWorkPolicy.KEEP,
//            PeriodicWorkRequest.Builder(
//                WeatherWidgetWorker::class.java,
//                60.minutes.toJavaDuration()
//            ).build()
//        )
//
//        provideContent {
//            val weatherViewModel = WeatherViewModel()
//            weatherViewModel.fetchWeatherByCurrentLocation(context, apiKey)
//
//            val weatherData by remember { weatherViewModel.weatherData }
//            val lastUpdateTime by rememberUpdatedState(newValue = getCurrentTime())
//
//            Column(
//                modifier = GlanceModifier
//                    .fillMaxSize()
//                    .background(Color.Gray),
//                verticalAlignment = Alignment.Vertical.CenterVertically,
//                horizontalAlignment = Alignment.Horizontal.CenterHorizontally
//            ) {
//                if (isUserLoggedIn && (checkLocationPermissions(context))) {
//                    weatherData?.let { data ->
//                        val temperatureCelsius = (data.main.temp - 273).toInt()
//                        val feelsLikeCelsius = (data.main.feels_like - 273).toInt()
//                        Text(
//                            text = "Темп: $temperatureCelsius°C\nОщущается как: $feelsLikeCelsius°C\nГород: ${data.name}\nПоследнее обновление: $lastUpdateTime",
//                            style = TextStyle(fontSize = 20.sp)
//                        )
//                        LaunchedEffect(key1 = data.weather[0].icon) {
//                            iconBitmap = loadImageAsBitmap("https://openweathermap.org/img/w/${data.weather[0].icon}.png")
//                        }
//                        Image(
//                            modifier = GlanceModifier.size(40.dp),
//                            provider = if (iconBitmap != null) {
//                                ImageProvider(iconBitmap!!)
//                            } else {
//                                ImageProvider(R.drawable.ic_launcher_foreground)
//                            },
//                            contentDescription = "Weather icon")
//
//                        Button(
//                            text = "Обновить",
//                            onClick = actionRunCallback(RefreshWeather::class.java)
//                        )
//                    } ?: run {
//                        Text(
//                            text = "Загрузка погоды...",
//                            style = TextStyle(
//                                fontSize = 20.sp
//                            )
//                        )
//                    }
//                } else if(!isUserLoggedIn) {
//                    Text(
//                        text = "Необходимо авторизоваться",
//                        style = TextStyle(
//                            fontSize = 20.sp
//                        )
//                    )
//                } else if(!(checkLocationPermissions(context))) {
//                    Text(
//                        text = "Нужно разрешение на геолокацию",
//                        style = TextStyle(
//                            fontSize = 20.sp
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    private suspend fun loadImageAsBitmap(url: String): Bitmap? {
//        return try {
//            withContext(Dispatchers.IO) {
//                BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun getCurrentTime(): String {
//        val currentTime = LocalTime.now()
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//        return currentTime.format(formatter)
//    }
//}
//
//
//class Simple : GlanceAppWidgetReceiver() {
//    override val glanceAppWidget: GlanceAppWidget
//        get() = WeatherWidget
//}
//
//class RefreshWeather : ActionCallback {
//    override suspend fun onAction(
//        context: Context,
//        glanceId: GlanceId,
//        parameters: ActionParameters
//    ) {
//        WeatherWidget.update(context, glanceId)
//    }
//}
//
//class WeatherWidgetWorker(
//    appContext: Context,
//    params: WorkerParameters
//) : CoroutineWorker(appContext, params) {
//    override suspend fun doWork(): Result {
//        WeatherWidget.apply {
//            updateAll(applicationContext)
//        }
//        return Result.success()
//    }
//}
//
//private fun checkLocationPermissions(context: Context): Boolean {
//    val fineLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    val coarseLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//
//    return fineLocationPermission && coarseLocationPermission
//}
