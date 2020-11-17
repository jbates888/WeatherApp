package com.example.forecast.data.api.response

import com.google.gson.annotations.SerializedName
import com.example.forecast.data.database.entity.CurrentWeatherEntry
import com.example.forecast.data.database.entity.WeatherLocation
import com.example.forecast.data.database.entity.Request

data class CurrentWeatherResponse(
    val request: Request,
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)