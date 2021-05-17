package com.yosemiteyss.greentransit.app.stop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.app.stop.StopRoutesListModel.StopRouteEmptyModel
import com.yosemiteyss.greentransit.app.stop.StopRoutesListModel.StopRouteItemModel
import com.yosemiteyss.greentransit.domain.models.StopRouteWithCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.stop.GetStopRoutesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by kevin on 17/5/2021
 */

class StopRoutesViewModel @AssistedInject constructor(
    private val getStopRoutesUseCase: GetStopRoutesUseCase,
    @Assisted stopId: Long
) : ViewModel() {

    private val _stopRoutesUiState = MutableStateFlow<StopRoutesUiState>(StopRoutesUiState.Loading)
    val stopRoutesUiState: StateFlow<StopRoutesUiState> = _stopRoutesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getStopRoutesUseCase(stopId).map { res ->
                when (res) {
                    is Resource.Success -> StopRoutesUiState.Success(
                        buildStopRouteListModel(res.data)
                    )
                    is Resource.Error -> StopRoutesUiState.Error(
                        listOf(StopRouteEmptyModel), res.message
                    )
                    is Resource.Loading -> StopRoutesUiState.Loading
                }
            }.collect {
                _stopRoutesUiState.value = it
            }
        }
    }

    private fun buildStopRouteListModel(
        stopRouteWithCodes: List<StopRouteWithCode>
    ): List<StopRoutesListModel> {
        if (stopRouteWithCodes.isEmpty())
            return listOf(StopRouteEmptyModel)

        return stopRouteWithCodes.map {
            StopRouteItemModel(it)
        }
    }

    @dagger.assisted.AssistedFactory
    interface StopRoutesViewModelFactory {
        fun create(stopId: Long): StopRoutesViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: StopRoutesViewModelFactory,
            stopId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(stopId) as T
            }
        }
    }
}

sealed class StopRoutesUiState {
    data class Success(val data: List<StopRoutesListModel>) : StopRoutesUiState()
    data class Error(val data: List<StopRoutesListModel>, val message: String?) : StopRoutesUiState()
    object Loading : StopRoutesUiState()
}