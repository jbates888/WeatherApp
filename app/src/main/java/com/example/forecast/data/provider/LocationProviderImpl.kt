package com.example.forecast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.forecast.data.database.entity.WeatherLocation
import com.example.forecast.background.asDeferredAsync
import kotlinx.coroutines.Deferred
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

/*
 * Class getting the users current location
 */
class LocationProviderImpl(private val fusedLocationProviderClient: FusedLocationProviderClient, context: Context) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    //check if the location needs to be updated
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: Throwable) {
            false
        }
        //return if the gps location has changed or if the user has typed a new location
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    //get the location that the user wants, either gps or typed
    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocationAsync().await()
                    ?: return "${getCustomLocationName()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (e: Throwable) {
                return "${getCustomLocationName()}"
            }
        } else {
            return "${getCustomLocationName()}"
        }
    }

    //if the gps location has changed return true
    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        //if the device is not using gps, return false
        if (!isUsingDeviceLocation()) return false
        //get the current location
        val deviceLocation = getLastDeviceLocationAsync().await() ?: return false
        //return if the current location is far enough away from the the last location to update the app
        return abs(deviceLocation.latitude - lastWeatherLocation.lat) > .3 && abs(deviceLocation.longitude - lastWeatherLocation.lon) > .3
    }

    //if the users enterd location has changed
    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val customLocationName = getCustomLocationName()
        return customLocationName != lastWeatherLocation.name
    }

    //return if the user has enabled location services
    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    //get the last known location
    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocationAsync(): Deferred<Location?> {
        return fusedLocationProviderClient.lastLocation.asDeferredAsync()
    }
    //check if the user is allowing the app to use GPS
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}