package com.example.jetpackproject.activities.greetings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.jetpackproject.ui.theme.JetpackProjectTheme

class Greetings : ComponentActivity() {
    private val weatherViewModel by viewModels<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackProjectTheme {
                WeatherScreen(weatherViewModel, this)
            }
        }
    }
}

