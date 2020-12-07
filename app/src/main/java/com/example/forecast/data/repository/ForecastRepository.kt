package com.example.forecast.data.repository

import androidx.lifecycle.LiveData
import com.example.forecast.data.database.entity.CurrentWeatherEntry
import com.example.forecast.data.database.entity.WeatherLocation

/*
 * Set up the repository interface
 */
interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}