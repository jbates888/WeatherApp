package com.example.forecast.internal

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/*
 * Class for converting GSON from the api
 */
class Converters {
    private val gson = Gson()

    //convert list to string
    @TypeConverter
    fun listToObj(list: List<String>): String {
        return gson.toJson(list)
    }

    //convert strings to list
    @TypeConverter
    fun objectsToList(value: String?): List<String> {
        if (value == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}