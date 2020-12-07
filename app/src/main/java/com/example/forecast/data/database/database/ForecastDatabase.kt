package com.example.forecast.data.database.database

import android.content.Context
import androidx.room.*
import com.example.forecast.data.database.dao.CurrentWeatherDao
import com.example.forecast.data.database.dao.WeatherLocationDao
import com.example.forecast.data.database.entity.CurrentWeatherEntry
import com.example.forecast.data.database.entity.WeatherLocation
import com.example.forecast.internal.Converters

/*
 * Class for data base to keep recent weather location loaded in the app, avoids extra api calls
 */
@Database(entities = [CurrentWeatherEntry::class, WeatherLocation::class], version = 1)
@TypeConverters(value = [(Converters::class)])
abstract class ForecastDatabase : RoomDatabase() {
    //create both of the Daos here
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        //build the database
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, ForecastDatabase::class.java, "forecast.db").build()
    }
}