package com.example.jetpackproject.di

import com.example.jetpackproject.data.location.DefaultLocationTracker
import com.example.jetpackproject.data.repository.WeatherRepositoryImpl
import com.example.jetpackproject.domain.location.LocationTracker
import com.example.jetpackproject.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}