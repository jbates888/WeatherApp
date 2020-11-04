package com.example.forecast.data.db.database

import android.content.Context
import androidx.room.*
import com.example.forecast.data.db.dao.CurrentWeatherDao
import com.example.forecast.data.db.dao.WeatherLocationDao
import com.example.forecast.data.db.entity.CurrentWeatherEntry
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.internal.Converters

@Database(entities = [CurrentWeatherEntry::class, WeatherLocation::class], version = 1)
@TypeConverters(value = [(Converters::class)])
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java,
                "forecast.db"
            ).build()
    }
}