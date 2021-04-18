package com.example.chefling.ui

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.example.chefling.R
import com.example.chefling.adapter.DailyAdapter
import com.example.chefling.adapter.HourlyAdapter
import com.example.chefling.databinding.FragmentHomeBinding
import com.example.chefling.utils.DataState
import com.example.chefling.viewmodels.ActivityViewModel
import com.example.localdatabase.Entity
import com.example.localdatabase.RoomDao
import com.example.myapiservicesmodule.di.models.ResponseOfForecastApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var roomDao: RoomDao

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val activityViewModel: ActivityViewModel by viewModels()
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var linearLayoutManagerHourly: LinearLayoutManager
    private lateinit var linearLayoutManagerDaily: LinearLayoutManager
    private var hourlyAdapter: HourlyAdapter? = null
    private var dailyAdapter: DailyAdapter? = null
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManagerHourly =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManagerDaily =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        if (sharedPreferences.getBoolean("autoRefresh", true))
            fragmentHomeBinding.btTurnoff.text = getString(R.string.turnoff)
        else
            fragmentHomeBinding.btTurnoff.text = getString(R.string.restart)
        setClickListener()

        observeSuccessStatusAfterCallingForecastApi()
        checkGpsEnabledOrNot()
        observeLocationPermissionStatus()
        observeForecastFromLocaldatabase()
    }

    private fun setClickListener() {
        editor = sharedPreferences.edit()
        fragmentHomeBinding.btTurnoff.setOnClickListener {
            // cancel the auto refresh
            if (fragmentHomeBinding.btTurnoff.text.toString().equals(getString(R.string.turnoff))) {
                fragmentHomeBinding.btTurnoff.text = getString(R.string.restart)
                editor.putBoolean("autoRefresh", false)
                editor.apply()
                WorkManager.getInstance(requireActivity())
                    .cancelUniqueWork(getString(R.string.uniqueWorkName))
                Toast.makeText(requireActivity(),getString(R.string.refreshCanceledToastmsg),Toast.LENGTH_LONG).show()
            }
            // restart auto refresh
            else if (fragmentHomeBinding.btTurnoff.text.toString()
                    .equals(getString(R.string.restart))
            ) {
                editor.putBoolean("autoRefresh", true)
                editor.apply()
                fragmentHomeBinding.btTurnoff.text = getString(R.string.turnoff)
                activityViewModel.startWorkManager()
            Toast.makeText(requireActivity(),getString(R.string.refreshRestartedToastmsg),Toast.LENGTH_LONG).show()
            }
        }
        fragmentHomeBinding.btRefresh.setOnClickListener {
            // Call the Api to refresh UI
            activityViewModel.callApiToRefreshUI(ActivityViewModel.MainStateEvent.GetApiResponse)
        }
    }

    // Data observed from inside the Roomdatabase
    private fun observeForecastFromLocaldatabase() {
        roomDao.getForecast().observe(viewLifecycleOwner, Observer {
            // Called  when database is updated.
            if (it.isNotEmpty()) {
                setData(it.get(0))
            }
            // called when Roomdatabase is empty
            else {
                fragmentHomeBinding.rlDatacontainer.visibility = View.GONE

            }
        })
    }

    private fun observeSuccessStatusAfterCallingForecastApi() {
        activityViewModel.apiResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<ResponseOfForecastApi> -> {
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Data updated successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is DataState.Error -> {
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                }
                is DataState.Loading -> {
                    if (!fragmentHomeBinding.progressbar.isVisible)
                        fragmentHomeBinding.progressbar.visibility = View.VISIBLE
                }
            }
        })
    }

    // Check GPS enabled or not
    private fun checkGpsEnabledOrNot() {
        if (activityViewModel.isGPSEnabled()) {
            activityViewModel.checkLocationPermission()
        } else {
            Toast.makeText(
                requireActivity(),
                "Please turn on the GPS and restart the App.",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun observeLocationPermissionStatus() {
        activityViewModel.LiveDataLocationPermission.observe(viewLifecycleOwner, Observer {
            if (it) {
                activityViewModel.getLastKnownLocation()
            } else {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 2000
                )
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2000 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted.
                    activityViewModel.getLastKnownLocation()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.permission_string),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    ActivityCompat.finishAffinity(requireActivity())
                }
                return
            }
        }
    }

    private fun setData(entity: Entity) {
        fragmentHomeBinding.rlDatacontainer.visibility = View.VISIBLE
        fragmentHomeBinding.progressbar.visibility = View.GONE
        fragmentHomeBinding.tvCurrentTemp.text = entity.current.temp
        fragmentHomeBinding.tvCurrentPressure.text = entity.current.pressure
        fragmentHomeBinding.tvCurrentWindspeed.text = entity.current.wind_speed
        fragmentHomeBinding.tvCurrentHumidity.text = entity.current.humidity

        activityViewModel.hourlyList.clear()
        activityViewModel.hourlyList.addAll(entity.hourly)
        activityViewModel.dailyList.clear()
        activityViewModel.dailyList.addAll(entity.daily)

        if (hourlyAdapter == null) {
            hourlyAdapter = HourlyAdapter(requireActivity(), activityViewModel.hourlyList)
            fragmentHomeBinding.rvHourly.layoutManager = linearLayoutManagerHourly
            fragmentHomeBinding.rvHourly.adapter = hourlyAdapter
        } else
            hourlyAdapter?.notifyDataSetChanged()


        if (dailyAdapter == null) {
            dailyAdapter = DailyAdapter(requireActivity(), activityViewModel.dailyList)
            fragmentHomeBinding.rvDaily.layoutManager = linearLayoutManagerDaily
            fragmentHomeBinding.rvDaily.adapter = dailyAdapter
        } else
            dailyAdapter?.notifyDataSetChanged()
    }

}