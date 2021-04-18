package com.example.localdatabase

import androidx.room.TypeConverter
import com.example.myapiservicesmodule.di.models.Current
import com.example.myapiservicesmodule.di.models.Daily
import com.example.myapiservicesmodule.di.models.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    companion object {

        @TypeConverter
        @JvmStatic
        fun fromHourlyList(hourlyList: List<Hourly>): String? {
            val gson = Gson()
            val type = object : TypeToken<List<Hourly>>() {}.type
            return gson.toJson(hourlyList, type)
        }

        @TypeConverter
        @JvmStatic
        fun toHourlyList(hourlyString: String): List<Hourly>? {
            val gson = Gson()
            val type = object : TypeToken<List<Hourly>>() {}.type
            return gson.fromJson<List<Hourly>>(hourlyString, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromDailyList(dailyList: List<Daily>): String? {
            val gson = Gson()
            val type = object : TypeToken<List<Daily>>() {}.type
            return gson.toJson(dailyList, type)
        }

        @TypeConverter
        @JvmStatic
        fun toDailyList(dailyString: String): List<Daily>? {
            val gson = Gson()
            val type = object : TypeToken<List<Daily>>() {}.type
            return gson.fromJson<List<Daily>>(dailyString, type)
        }


        @TypeConverter
        @JvmStatic
        fun fromCurrent(currentForecast: Current): String? {
            val gson = Gson()
            return gson.toJson(currentForecast)
        }

        @TypeConverter
        @JvmStatic
        fun toCurrent(currentForecastString: String): Current {
            val gson = Gson()
            val type = object : TypeToken<Current>() {}.type
            return gson.fromJson(currentForecastString, type)
        }
    }
}