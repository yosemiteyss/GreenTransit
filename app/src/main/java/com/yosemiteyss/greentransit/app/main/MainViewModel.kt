package com.yosemiteyss.greentransit.app.main

import android.location.Location
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
import kotlinx.coroutines.channels.Channel
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

    private val _mapEnabled = MutableStateFlow(false)
    val mapEnabled: StateFlow<Boolean> = _mapEnabled.asStateFlow()

    private val _locationInput = MutableSharedFlow<Location>()

    private val _bearingInput = MutableSharedFlow<Float>()

    private val _userLocation = MutableSharedFlow<Location>()
    val userLocation: SharedFlow<Location> = _userLocation.asSharedFlow()

    private val _nearbyStops = MutableStateFlow<List<NearbyStop>>(emptyList())
    val nearbyStops: StateFlow<List<NearbyStop>> = _nearbyStops.asStateFlow()

    private val _toastMessage = Channel<String>()
    val toastMessage: Flow<String> = _toastMessage.receiveAsFlow()

    init {
        // Build user location
        viewModelScope.launch {
            _bearingInput.combine(_locationInput, ::buildLocation).collect {
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
                    is Resource.Error -> onShowToastMessage(it.message)
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    fun onEnableMap(isEnabled: Boolean) {
        _mapEnabled.value = isEnabled
    }

    fun onUpdateLocation(location: Location) = viewModelScope.launch {
        _locationInput.emit(location)
    }

    fun onUpdateBearing(bearing: Float) = viewModelScope.launch {
        _bearingInput.emit(bearing)
    }

    fun onShowToastMessage(message: String?) {
        message?.let {
            _toastMessage.offer(it)
        }
    }

    private fun buildLocation(bearing: Float, location: Location): Location {
        return Location("").apply {
            latitude = location.latitude
            longitude = location.longitude
            this.bearing = bearing
        }
    }
}