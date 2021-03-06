//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.route

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopEmptyModel
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopItemModel
import com.yosemiteyss.greentransit.domain.models.*
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.route.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

private const val FETCH_ETAS_INTERVAL = 20000L

class RouteViewModel @AssistedInject constructor(
    getRouteInfoUseCase: GetRouteInfoUseCase,
    getRouteStopShiftEtasUseCase: GetRouteStopShiftEtasUseCase,
    getRouteStopInfosUseCase: GetRouteStopInfosUseCase,
    @Assisted routeOption: RouteOption
): ViewModel() {

    private val _routeInfos = MutableStateFlow<Resource<List<RouteInfo>>>(Resource.Loading())
    val routeInfos: StateFlow<Resource<List<RouteInfo>>> = _routeInfos.asStateFlow()

    private val _currentRouteId = MutableStateFlow<Long?>(null)

    private val _currentDirection = MutableStateFlow<RouteDirection?>(null)
    val currentDirection: StateFlow<RouteDirection?> = _currentDirection.asStateFlow()

    private val _stopShiftEtaResults = MutableStateFlow<Resource<List<RouteStopShiftEtaResult>>>(
        Resource.Loading()
    )

    private val _stopsListModels = MutableStateFlow<Resource<List<RouteStopsListModel>>>(Resource.Loading())
    val stopsListModels: StateFlow<Resource<List<RouteStopsListModel>>> = _stopsListModels.asStateFlow()

    private val _directionStopsInfos = MutableStateFlow<Resource<List<StopInfo>>>(Resource.Loading())
    val directionStopsInfos: StateFlow<Resource<List<StopInfo>>> = _directionStopsInfos.asStateFlow()

    init {
        // Get route info
        viewModelScope.launch {
            val param = GetRouteInfoParameters(
                routeId = routeOption.routeId,
                routeCode = routeOption.routeRegionCode
            )

            getRouteInfoUseCase(param).collect { res ->
                _routeInfos.value = when (res) {
                    is Resource.Success -> {
                        val routeInfo = res.data
                        _currentRouteId.value = routeInfo.first().routeId
                        _currentDirection.value = routeInfo.first().directions.first()

                        Resource.Success(routeInfo)
                    }
                    is Resource.Error -> Resource.Error(res.message)
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }

        // Get stop etas
        viewModelScope.launch {
            _currentRouteId.filterNotNull()
                .combine(_currentDirection.filterNotNull()) { routeId, direction ->
                    GetRouteStopShiftEtasParameters(
                        routeId = routeId,
                        routeSeq = direction.routeSeq,
                        interval = FETCH_ETAS_INTERVAL
                    )
                }
                .flatMapLatest { getRouteStopShiftEtasUseCase(it) }
                .collect {
                    _stopShiftEtaResults.value = it
                }
        }

        // Build stops list models
        viewModelScope.launch {
            _stopShiftEtaResults.map { res ->
                    when (res) {
                        is Resource.Success -> Resource.Success(buildRouteStopsListModels(results = res.data))
                        is Resource.Error -> Resource.Error(res.message)
                        is Resource.Loading -> Resource.Loading()
                    }
                }
                .collect {
                    _stopsListModels.value = it
                }
        }

        // Get direction's stops
        viewModelScope.launch {
            _stopShiftEtaResults.map { res ->
                    when (res) {
                        is Resource.Success -> res.data.map { it.routeStop.stopId }
                        else -> emptyList()
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { getRouteStopInfosUseCase(it) }
                .collect {
                    _directionStopsInfos.value = it
                }
        }
    }

    fun onUpdateCurrentDirection(routeId: Long, routeSeq: Int) {
        val routeInfo = _routeInfos.value

        if (routeInfo is Resource.Success) {
            _currentDirection.value = routeInfo.data
                .first { it.routeId == routeId }
                .directions
                .first { it.routeSeq == routeSeq }
        }
    }

    private fun buildRouteStopsListModels(
        results: List<RouteStopShiftEtaResult>
    ) : List<RouteStopsListModel> {
        if (results.isEmpty()) {
            return listOf(RouteStopEmptyModel)
        }

        return results.map { result ->
            RouteStopItemModel(
                stopId = result.routeStop.stopId,
                stopSeq = result.routeStop.stopSeq,
                stopName = result.routeStop.stopName,
                etaEnabled = result.routeStopShiftEta?.etaEnabled ?: false,
                etaDescription = result.routeStopShiftEta?.etaDescription,
                etaMin = result.routeStopShiftEta?.etaMin,
                etaDate = result.routeStopShiftEta?.etaDate,
                etaRemarks = result.routeStopShiftEta?.etaRemarks
            )
        }
    }

    @dagger.assisted.AssistedFactory
    interface RouteViewModelFactory {
        fun create(routeOption: RouteOption): RouteViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RouteViewModelFactory,
            routeOption: RouteOption
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(routeOption) as T
            }
        }
    }
}

@Parcelize
data class RouteOption(
    val routeRegionCode: @RawValue RouteCode? = null,
    val routeId: Long? = null,
    val routeCode: String? = null
) : Parcelable