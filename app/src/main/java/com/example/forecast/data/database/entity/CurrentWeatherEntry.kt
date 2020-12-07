package com.example.forecast.data.database.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.example.forecast.internal.Converters

const val CURRENT_WEATHER_ID = 0

/*
 * Class sets type for all the items included with a response from the api
 */
@Entity(tableName = "current_weather")
data class CurrentWeatherEntry(
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Double,
    @SerializedName("weather_icons")
    @TypeConverters(Converters::class)
    val weatherIcons: List<String>,
    @SerializedName("weather_descriptions")
    @TypeConverters(Converters::class)
    val weatherDescriptions: List<String>,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_dir")
    val windDir: String,
    val precip: Double,
    val feelslike: Double,
    @SerializedName("uv_index")
    val uvIndex: Double,
    val visibility: Double,
    @SerializedName("is_day")
    val isDay: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}