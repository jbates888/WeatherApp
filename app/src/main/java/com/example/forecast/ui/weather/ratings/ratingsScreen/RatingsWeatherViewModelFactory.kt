package com.example.forecast.ui.weather.ratings.ratingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.provider.UnitProvider
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.ui.weather.current.CurrentWeatherViewModel

class RatingsWeatherViewModelFactory(private val forecastRepository: ForecastRepository, private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RatingsWeatherViewModel(forecastRepository, unitProvider) as T
    }
}