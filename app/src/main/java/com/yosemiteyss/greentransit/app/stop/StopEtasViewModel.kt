package com.yosemiteyss.greentransit.app.stop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.app.stop.StopEtasListModel.StopEtasEmptyModel
import com.yosemiteyss.greentransit.app.stop.StopEtasListModel.StopEtasShiftModel
import com.yosemiteyss.greentransit.domain.models.StopEtaShiftWithCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.stop.GetStopEtaShiftsParameters
import com.yosemiteyss.greentransit.domain.usecases.stop.GetStopEtaShiftsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by kevin on 18/5/2021
 */

private const val FETCH_ETAS_INTERVAL = 20000L

class StopEtasViewModel @AssistedInject constructor(
    private val getStopEtaShiftsUseCase: GetStopEtaShiftsUseCase,
    @Assisted stopId: Long
) : ViewModel() {

    private val _stopEtasUiState = MutableStateFlow<StopEtasUiState>(StopEtasUiState.Loading)
    val stopEtasUiState: StateFlow<StopEtasUiState> = _stopEtasUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val param = GetStopEtaShiftsParameters(
                stopId = stopId,
                interval = FETCH_ETAS_INTERVAL
            )

            getStopEtaShiftsUseCase(param).map { res ->
                when (res) {
                    is Resource.Success -> StopEtasUiState.Success(
                        buildStopEtasListModels(res.data)
                    )
                    is Resource.Error -> StopEtasUiState.Error(
                        listOf(StopEtasEmptyModel), res.message
                    )
                    is Resource.Loading -> StopEtasUiState.Loading
                }
            }.collect {
                _stopEtasUiState.value = it
            }
        }
    }

    private fun buildStopEtasListModels(
        stopEtaShiftWithCodes: List<StopEtaShiftWithCode>
    ): List<StopEtasListModel> {
        if (stopEtaShiftWithCodes.isEmpty())
            return listOf(StopEtasEmptyModel)

        return stopEtaShiftWithCodes.map {
            StopEtasShiftModel(it)
        }
    }

    @dagger.assisted.AssistedFactory
    interface StopEtasModelFactory {
        fun create(stopId: Long): StopEtasViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: StopEtasModelFactory,
            stopId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(stopId) as T
            }
        }
    }
}

sealed class StopEtasUiState {
    data class Success(val data: List<StopEtasListModel>) : StopEtasUiState()
    data class Error(val data: List<StopEtasListModel>, val message: String?) : StopEtasUiState()
    object Loading : StopEtasUiState()
}