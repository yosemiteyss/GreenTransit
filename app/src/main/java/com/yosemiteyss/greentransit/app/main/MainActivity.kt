//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.ActivityMainBinding
import com.yosemiteyss.greentransit.app.utils.isPermissionGranted
import com.yosemiteyss.greentransit.app.utils.launchAppSettings
import com.yosemiteyss.greentransit.app.utils.setLayoutFullscreen
import com.yosemiteyss.greentransit.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()

    private val requestLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onEnableMap(true)
        } else {
            showLocationDeniedSnackbar()
        }
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
            viewModel.onEnableMap(true)
        } else {
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container)
            as NavHostFragment
        val navController = navHostFragment.navController
        val topDestinations = listOf(R.id.homeFragment, R.id.newsFragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNavView.isVisible = destination.id in topDestinations
        }

        binding.bottomNavView.setupWithNavController(navController)
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