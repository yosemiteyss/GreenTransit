package com.yosemiteyss.greentransit.data.repositories

import android.Manifest
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_UI
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.yosemiteyss.greentransit.data.utils.locationFlow
import com.yosemiteyss.greentransit.data.utils.orientationFlow
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by kevin on 5/6/2021
 */

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val sensorManager: SensorManager
) : LocationRepository {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getDeviceLocation(): Flow<Coordinate> {
        val locationRequest = LocationRequest.create().apply {
            this.interval = 5000L
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return fusedLocationProviderClient.locationFlow(locationRequest)
            .map { Coordinate(it.latitude, it.longitude) }
    }

    override fun getDeviceOrientation(): Flow<Float> {
        return sensorManager.orientationFlow(SENSOR_DELAY_UI)
            .map { it.azimuth }
    }
}