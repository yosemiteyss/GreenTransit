package com.yosemiteyss.greentransit.app.stop

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.StopInfo
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.stop.GetStopInfoUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Created by kevin on 17/5/2021
 */

class StopViewModel @AssistedInject constructor(
    getStopInfoUseCase: GetStopInfoUseCase,
    @Assisted val stopId: Long
) : ViewModel() {

    val stopPages: StateFlow<List<StopPage>> = flowOf(listOf(
        StopPage(
            titleRes = R.string.stop_tab_eta,
            fragment = { StopEtasFragment() }
        ),
        StopPage(
            titleRes = R.string.stop_tab_routes,
            fragment = { StopRoutesFragment() }
        ),
    )).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val stopUiState: StateFlow<StopUiState?> = getStopInfoUseCase(stopId)
        .map { res ->
            when (res) {
                is Resource.Success -> StopUiState.Success(res.data)
                is Resource.Error -> StopUiState.Error(res.message)
                is Resource.Loading -> StopUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    @dagger.assisted.AssistedFactory
    interface StopViewModelFactory {
        fun create(stopId: Long): StopViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: StopViewModelFactory,
            stopId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(stopId) as T
            }
        }
    }
}

sealed class StopUiState {
    data class Success(val data: StopInfo) : StopUiState()
    data class Error(val message: String?) : StopUiState()
    object Loading : StopUiState()
}

@Parcelize
data class StopProperty(
    val stopId: Long,
    val coordinate: @RawValue Coordinate
) : Parcelable