package com.example.forecast.data.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.data.api.response.CurrentWeatherResponse


/*
 * Class for fetching the current weather
 */
class WeatherNetworkDataSourceImpl(private val weatherStackApiService: WeatherStackApiService) : WeatherNetworkDataSource {
    private val getCurWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = getCurWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        val fetchCurrentWeather = weatherStackApiService.getCurrentWeatherAsync(location, languageCode).await()
        getCurWeather.postValue(fetchCurrentWeather)



    }
}