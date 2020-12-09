package com.example.forecast.foreground.weather.ratings.ratingsScreen

import androidx.lifecycle.ViewModel
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.background.lazyDeferred

/*
 * View model for the ratings fragment
 */
class RatingsWeatherViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
