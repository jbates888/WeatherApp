package com.example.forecast.foreground.weather.ratings.ratingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.provider.UnitProvider
import com.example.forecast.data.repository.ForecastRepository

/*
 * View model factory for the ratings fragment
 */
class RatingsWeatherViewModelFactory(private val forecastRepository: ForecastRepository, private val unitProvider: UnitProvider) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RatingsWeatherViewModel(forecastRepository) as T
    }
}