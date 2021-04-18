package com.example.localdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapiservicesmodule.di.models.Current
import com.example.myapiservicesmodule.di.models.Daily
import com.example.myapiservicesmodule.di.models.Hourly

@Entity(tableName = "forecastTable")
class Entity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int = 0,
        var lat: String,
        var lon: String,
        @TypeConverters(DataConverter::class)
        val current: Current,
        @TypeConverters(DataConverter::class)
        val hourly: List<Hourly>,
        @TypeConverters(DataConverter::class)
        val daily: List<Daily>
)