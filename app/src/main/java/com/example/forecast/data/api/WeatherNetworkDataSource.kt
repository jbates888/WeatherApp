package com.example.forecast.data.api

import androidx.lifecycle.LiveData
import com.example.forecast.data.api.response.CurrentWeatherResponse

/*
 * Class to set up request to get weather
 */
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    //pass in the location and the language
    suspend fun fetchCurrentWeather(location: String, languageCode: String)
}