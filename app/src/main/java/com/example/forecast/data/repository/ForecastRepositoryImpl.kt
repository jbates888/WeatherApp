package com.example.forecast.data.repository

import androidx.lifecycle.LiveData
import com.example.forecast.data.database.dao.CurrentWeatherDao
import com.example.forecast.data.database.dao.WeatherLocationDao
import com.example.forecast.data.database.entity.CurrentWeatherEntry
import com.example.forecast.data.database.entity.WeatherLocation
import com.example.forecast.data.api.WeatherNetworkDataSource
import com.example.forecast.data.api.response.CurrentWeatherResponse
import com.example.forecast.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

/*
 * Class for executing weather commands
 */
class ForecastRepositoryImpl(private val currentWeatherDao: CurrentWeatherDao, private val weatherLocationDao: WeatherLocationDao,
                             private val weatherNetworkDataSource: WeatherNetworkDataSource, private val locationProvider: LocationProvider) : ForecastRepository {

    //checks if 15 minuets has passed since the last fetch
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(15)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
    //override and get the current weather
    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry> {
        initWeatherData()
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeather()
        }
    }
    //override and get the location
    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }
    //fetch the weather data if its needed
    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)){
            fetchCurrentWeather()
        }
    }

    //get the current weather
    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString(), Locale.getDefault().language)
    }

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever {
            persistFetchedCurrentWeather(it)
        }
    }
}