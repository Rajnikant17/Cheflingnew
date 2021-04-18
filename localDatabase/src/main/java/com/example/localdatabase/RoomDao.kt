package com.example.localdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {

    @Insert
    suspend fun insertItem(entity: Entity) :Long

    @Query("DELETE FROM forecastTable")
    suspend fun delete()

    @Query("select * from  forecastTable")
    fun getForecast(): LiveData<List<Entity>>

}