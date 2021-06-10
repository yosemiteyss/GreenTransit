//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.models.NearbyRoute
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNearbyRoutesUseCase: GetNearbyRoutesUseCase
) : ViewModel() {

    private val _nearbyRoutesCount = MutableStateFlow(0)
    val nearbyRoutesCount: StateFlow<Int> = _nearbyRoutesCount.asStateFlow()

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    fun fetchNearbyRoutes(nearbyStops: List<NearbyStop>) = viewModelScope.launch {
        getNearbyRoutesUseCase(nearbyStops).collect { res ->
            _homeUiState.value = when (res) {
                is Resource.Success -> {
                    _nearbyRoutesCount.value = res.data.size
                    HomeUiState.Success(buildNearbyRoutesListModels(res.data))
                }
                is Resource.Error -> HomeUiState.Error(res.message)
                is Resource.Loading -> HomeUiState.Loading
            }
        }
    }

    private fun buildNearbyRoutesListModels(
        nearbyRouteEtas: List<NearbyRoute>
    ): List<NearbyRoutesListModel> = buildList {
        if (nearbyRouteEtas.isEmpty()){
            add(NearbyRoutesListModel.NearbyRoutesEmptyModel)
        } else {
            addAll(nearbyRouteEtas.map {
                NearbyRoutesListModel.NearbyRoutesItemModel(
                    routeId = it.id,
                    routeCode = it.code,
                    routeDest = it.dest
                )
            })
        }
    }
}

sealed class HomeUiState {
    data class Success(val data: List<NearbyRoutesListModel>) : HomeUiState()
    data class Error(val message: String?) : HomeUiState()
    object Loading : HomeUiState()
}