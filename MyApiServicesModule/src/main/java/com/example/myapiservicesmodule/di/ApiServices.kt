package com.example.myapiservicesmodule.di

import com.example.myapiservicesmodule.di.models.ResponseOfForecastApi
import retrofit2.http.*

interface ApiServices {
    @GET("data/2.5/onecall")
    suspend fun getForecast(@Query("lat") latitude:String?,@Query("lon") longitude:String?,@Query("exclude") exclude:String,@Query("APPID") APPID:String) : ResponseOfForecastApi

}