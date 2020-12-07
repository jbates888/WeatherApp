package com.example.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecast.data.provider.UnitProvider
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.internal.UnitSystem
import com.example.forecast.internal.lazyDeferred

/*
 * View model for the current weather fragment
 */
class CurrentWeatherViewModel(private val forecastRepository: ForecastRepository, unitProvider: UnitProvider) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}
