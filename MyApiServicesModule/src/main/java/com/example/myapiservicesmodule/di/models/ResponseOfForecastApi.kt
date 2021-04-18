package com.example.myapiservicesmodule.di.models
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
data class ResponseOfForecastApi(
       @SerializedName("lat")
        @Expose
        var lat: String,
        @SerializedName("lon")
        @Expose
        var lon: String,
        @SerializedName("current")
        @Expose
        var current: Current,
       @SerializedName("hourly")
       @Expose
       var hourly: List<Hourly>,
       @SerializedName("daily")
       @Expose
       var daily: List<Daily>
        )