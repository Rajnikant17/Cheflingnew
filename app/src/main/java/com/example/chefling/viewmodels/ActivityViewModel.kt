package com.example.chefling.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.chefling.R
import com.example.chefling.WorkerClass
import com.example.chefling.repository.BusinessLogic
import com.example.chefling.utils.DataState
import com.example.myapiservicesmodule.di.models.Daily
import com.example.myapiservicesmodule.di.models.Hourly
import com.example.myapiservicesmodule.di.models.ResponseOfForecastApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ActivityViewModel
@ViewModelInject
constructor(
    application: Application,
    private val apiCallBusinessLogic: BusinessLogic,
    val sharedPreferences: SharedPreferences
) :
    AndroidViewModel(application) {
    val globalContext = application
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val hourlyList = mutableListOf<Hourly>()
    val dailyList = mutableListOf<Daily>()
    var apiResponseLiveData: MutableLiveData<DataState<ResponseOfForecastApi>> = MutableLiveData()
    var LiveDataLocationPermission: MutableLiveData<Boolean> = MutableLiveData()

    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                globalContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                globalContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            LiveDataLocationPermission.value = false
            return
        } else {
            LiveDataLocationPermission.value = true
        }
    }

    fun isGPSEnabled() =
        (globalContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

    fun startWorkManager() {
        WorkManager.getInstance(globalContext).enqueueUniquePeriodicWork(
            globalContext.getString(R.string.uniqueWorkName),
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<WorkerClass>(12, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        )
    }

    fun callApiToRefreshUI(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetApiResponse -> {
                    apiCallBusinessLogic.callForecastApiTorRefreshUI(globalContext)
                        .onEach { dataState ->
                            apiResponseLiveData.value = dataState
                        }.launchIn(viewModelScope)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")// since the permission is supressed because this is called after taking permission
    fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(globalContext)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val editor = sharedPreferences.edit()
                editor.putString("latitude", location?.latitude.toString())
                editor.putString("longitude", location?.longitude.toString())
                editor.apply()

                // start workmanager when got the current location
                startWorkManager()
            }
    }

    sealed class MainStateEvent {
        object GetApiResponse : MainStateEvent()
    }
}