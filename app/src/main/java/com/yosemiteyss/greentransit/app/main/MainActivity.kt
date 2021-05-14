package com.yosemiteyss.greentransit.app.main

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.data.api.TrafficNewsService
import com.yosemiteyss.greentransit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()
    private var currentNavController: LiveData<NavController>? = null

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var sensorManager: SensorManager

    @SuppressLint("MissingPermission")
    private val requestLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            observerFusedLocation()
        } else {
            showLocationDeniedSnackbar()
        }
    }

    private val locationRequest: LocationRequest by lazy(LazyThreadSafetyMode.NONE) {
        LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = 5000L / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    @Inject
    lateinit var trafficNewsService: TrafficNewsService

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GreenTransit_DayNight)
        super.onCreate(savedInstanceState)
        setLayoutFullscreen(true)
        setContentView(binding.root)

        // Setup bottom nav
        if (savedInstanceState == null) {
            setupBottomNavigation()
        }

        // Get fused location
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            observerFusedLocation()
        } else {
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setupBottomNavigation() {
        val navGraphIds = listOf(R.navigation.navigation_home, R.navigation.navigation_news)
        currentNavController = binding.bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun observerFusedLocation() {
        viewModel.onEnableMap(true)

        // Get location
        addRepeatingJob(Lifecycle.State.STARTED) {
            fusedLocationProviderClient.locationFlow(locationRequest).collectLatest {
                viewModel.onUpdateLocation(it)
            }
        }

        // Get azimuth
        addRepeatingJob(Lifecycle.State.STARTED) {
            sensorManager.orientationFlow(SensorManager.SENSOR_DELAY_UI).collectLatest {
                viewModel.onUpdateBearing(it.azimuth)
            }
        }
    }

    private fun showLocationDeniedSnackbar() {
        Snackbar.make(
            binding.mainCoordinatorLayout,
            R.string.request_location_permission_message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.request_permission_action_settings) {
                launchAppSettings()
            }
            .show()
    }
}