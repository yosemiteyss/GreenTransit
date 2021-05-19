package com.yosemiteyss.greentransit.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.models.NearbyRoute
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNearbyRoutesUseCase: GetNearbyRoutesUseCase
) : ViewModel() {

    private val _nearbyRoutesCount = MutableStateFlow(0)
    val nearbyRoutesCount: StateFlow<Int> = _nearbyRoutesCount.asStateFlow()

    fun getNearbyRouteEtas(nearbyStops: StateFlow<List<NearbyStop>>): StateFlow<HomeUiState> {
        return nearbyStops.flatMapLatest { getNearbyRoutesUseCase(it) }
            .map { res ->
                when (res) {
                    is Resource.Success -> {
                        _nearbyRoutesCount.value = res.data.size
                        HomeUiState.Success(buildNearbyRoutesListModels(res.data))
                    }
                    is Resource.Error -> HomeUiState.Error(res.message)
                    is Resource.Loading -> HomeUiState.Loading
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = HomeUiState.Loading
            )
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