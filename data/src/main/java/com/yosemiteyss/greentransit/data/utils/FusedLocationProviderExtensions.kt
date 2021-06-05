//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.utils

import android.Manifest
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
fun FusedLocationProviderClient.locationFlow(
    locationRequest: LocationRequest
): Flow<Location> = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result?.let {
                channel.offer(it.lastLocation)
            }
        }
    }

    requestLocationUpdates(
        locationRequest,
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        channel.close(e)
    }

    awaitClose {
        removeLocationUpdates(callback)
    }
}