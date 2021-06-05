//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.main

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.ActivityMainBinding
import com.yosemiteyss.greentransit.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()
    private var currentNavController: LiveData<NavController>? = null

    @Inject
    lateinit var sensorManager: SensorManager

    private val requestLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) observerFusedLocation() else
            showLocationDeniedSnackbar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GreenTransit_DayNight)
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
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