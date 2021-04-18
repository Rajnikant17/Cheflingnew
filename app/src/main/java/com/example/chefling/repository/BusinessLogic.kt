package com.example.chefling.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.chefling.utils.DataState
import com.example.localdatabase.Entity
import com.example.localdatabase.RoomDao
import com.example.myapiservicesmodule.di.ApiServices
import com.example.myapiservicesmodule.di.models.ResponseOfForecastApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessLogic
@Inject
constructor(
    val apiServices: ApiServices,
    val roomDao: RoomDao,
    val sharedPreferences: SharedPreferences
) {

    suspend fun calledApiFromWorkerClass() {
        try {
            val responseOfForecastApi: ResponseOfForecastApi = apiServices.getForecast(
                sharedPreferences.getString("latitude", ""),
                sharedPreferences.getString("longitude", ""),
                "minutely",
                "9b8cb8c7f11c077f8c4e217974d9ee40"
            )
            uploadDataInDataBase(responseOfForecastApi)
            // upload in the database
        } catch (e: Exception) {

        }
    }

    suspend fun callForecastApiTorRefreshUI(context: Context): Flow<DataState<ResponseOfForecastApi>> =
        flow {
            emit(DataState.Loading)
            try {
                val responseOfForecastApi: ResponseOfForecastApi = apiServices.getForecast(
                    sharedPreferences.getString("latitude", ""),
                    sharedPreferences.getString("longitude", ""),
                    "minutely",
                    "9b8cb8c7f11c077f8c4e217974d9ee40"
                )
                emit(DataState.Success(responseOfForecastApi))
                uploadDataInDataBase(responseOfForecastApi)
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    suspend fun uploadDataInDataBase(responseOfForecastApi: ResponseOfForecastApi) {
        roomDao.delete()
        roomDao.insertItem(
            Entity(
                0,
                responseOfForecastApi.lat,
                responseOfForecastApi.lon,
                responseOfForecastApi.current,
                responseOfForecastApi.hourly,
                responseOfForecastApi.daily
            )
        )

    }
}