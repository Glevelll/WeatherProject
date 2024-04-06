package com.example.jetpackproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.jetpackproject.ui.theme.JetpackProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackProjectTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    PagerScreen()
                }
            }
        }
    }
}