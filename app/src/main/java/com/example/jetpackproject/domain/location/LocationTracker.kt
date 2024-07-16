package com.example.jetpackproject.domain.location

import android.health.connect.datatypes.ExerciseRoute.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): android.location.Location?
}