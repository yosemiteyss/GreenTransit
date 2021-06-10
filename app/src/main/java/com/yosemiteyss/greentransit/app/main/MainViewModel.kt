//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.utils.geohashQueryBounds
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.Location
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.models.round
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.location.GetDeviceLocationUseCase
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
    getDeviceLocationUseCase: GetDeviceLocationUseCase,
    private val getNearbyStopsUseCase: GetNearbyStopsUseCase
) : ViewModel() {

    private val _mapEnabled = MutableStateFlow(false)
    val mapEnabled: StateFlow<Boolean> = _mapEnabled.asStateFlow()

    private val _nearbyStops = MutableStateFlow<List<NearbyStop>>(emptyList())
    val nearbyStops: StateFlow<List<NearbyStop>> = _nearbyStops.asStateFlow()

    private val _toastMessage = Channel<String>()
    val toastMessage: Flow<String> = _toastMessage.receiveAsFlow()

    val userLocation: SharedFlow<Location> = getDeviceLocationUseCase()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1
        )

    val initialCoordinate: Coordinate = Coordinate(latitude = 22.302711, longitude = 114.177216)

    val topDestinations: List<Int> = listOf(R.id.homeFragment, R.id.newsFragment)

    init {
        // Build nearby stops
        viewModelScope.launch {
            userLocation.mapLatest { it.coordinate }
                .distinctUntilChangedBy { coordinate ->
                    // Accuracy: 11m
                    coordinate.round(4)
                }
                .mapLatest { coordinate ->
                    val queryBounds = LatLng(coordinate.latitude, coordinate.longitude)
                        .geohashQueryBounds(NEARBY_BOUND_METER.toDouble())
                    val nearbyBounds = queryBounds.map {
                        NearbyGeoBound(it.startHash, it.endHash)
                    }

                    GetNearbyStopsParams(currentCoord = coordinate, bounds = nearbyBounds)
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

    fun onShowToastMessage(message: String?) {
        message?.let {
            _toastMessage.offer(it)
        }
    }
}