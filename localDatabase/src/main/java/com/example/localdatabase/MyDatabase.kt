package com.example.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Entity::class] , version = 1,exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun roomDao() : RoomDao

    companion object {
        const val DATABASE_NAME="chefling_database"
    }
}