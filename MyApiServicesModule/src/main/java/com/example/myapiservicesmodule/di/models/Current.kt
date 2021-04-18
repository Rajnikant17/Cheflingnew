package com.example.myapiservicesmodule.di.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Current (
        @SerializedName("temp")
        @Expose
        var temp: String,
        @SerializedName("wind_speed")
        @Expose
        var wind_speed: String,
        @SerializedName("pressure")
        @Expose
        var pressure: String,
        @SerializedName("humidity")
        @Expose
        var humidity: String
        )