package com.example.forecast.foreground.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.provider.UnitProvider
import com.example.forecast.data.repository.ForecastRepository

/*
 * View model factory for the current weather fragment
 */
class CurrentWeatherViewModelFactory(private val forecastRepository: ForecastRepository, private val unitProvider: UnitProvider) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository, unitProvider) as T
    }
}