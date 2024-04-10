package com.example.jetpackproject.activities.LoginAndReg

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.jetpackproject.data.JetpackDatabase
import com.example.jetpackproject.ui.theme.JetpackProjectTheme

class MainActivity : ComponentActivity() {
    private lateinit var db: JetpackDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, JetpackDatabase::class.java, "users_db").build()
        setContent {
            JetpackProjectTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    PagerScreen(db, this@MainActivity)
                }
            }
        }
    }
}