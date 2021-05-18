package com.yosemiteyss.greentransit.app.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yosemiteyss.greentransit.app.utils.geohashQueryBounds
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyStopsParams
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyStopsUseCase
import com.yosemiteyss.greentransit.domain.usecases.nearby.NearbyGeoBound
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 11/5/2021
 */

private const val NEARBY_BOUND_METER = 300

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNearbyStopsUseCase: GetNearbyStopsUseCase
) : ViewModel() {

    private val _enableMap = MutableStateFlow(false)
    val enableMap: StateFlow<Boolean> = _enableMap.asStateFlow()

    private val _locationInput = MutableSharedFlow<Location>()
    private val _azimuthInput = MutableSharedFlow<Float>()

    private val _userLocation = MutableSharedFlow<Location>()
    val userLocation: SharedFlow<Location> = _userLocation.asSharedFlow()

    private val _nearbyStops = MutableStateFlow<List<NearbyStop>>(emptyList())
    val nearbyStops: StateFlow<List<NearbyStop>> = _nearbyStops.asStateFlow()

    init {
        // Build user location
        viewModelScope.launch {
            _locationInput.combine(_azimuthInput, ::buildLocation).collect {
                _userLocation.emit(it)
            }
        }

        // Build nearby stops
        viewModelScope.launch {
            _locationInput.mapLatest { center ->
                val queryBounds = LatLng(center.latitude, center.longitude)
                    .geohashQueryBounds(NEARBY_BOUND_METER.toDouble())

                val nearbyBounds = queryBounds.map {
                    NearbyGeoBound(it.startHash, it.endHash)
                }

                GetNearbyStopsParams(
                    currentCoord = Coordinate(center.latitude, center.longitude),
                    bounds = nearbyBounds
                )
            }.flatMapLatest {
                getNearbyStopsUseCase(it)
            }.collect {
                when (it) {
                    is Resource.Success -> _nearbyStops.emit(it.data)
                    is Resource.Error -> Log.d("MainViewModel", "${it.message}")
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    fun onEnableMap(isEnabled: Boolean) {
        _enableMap.value = isEnabled
    }

    fun onUpdateLocation(location: Location) = viewModelScope.launch {
        _locationInput.emit(location)
    }

    fun onUpdateBearing(bearing: Float) = viewModelScope.launch {
        _azimuthInput.emit(bearing)
    }

    private fun buildLocation(location: Location, azimuth: Float): Location {
        return Location("").apply {
            latitude = location.latitude
            longitude = location.longitude
            bearing = azimuth
        }
    }
}