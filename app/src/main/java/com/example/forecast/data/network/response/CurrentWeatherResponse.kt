package com.example.forecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.example.forecast.data.db.entity.CurrentWeatherEntry
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.db.entity.Request

data class CurrentWeatherResponse(
    val request: Request,
    val location: WeatherLocation,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)