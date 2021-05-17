package com.yosemiteyss.greentransit.app.stop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.R
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by kevin on 17/5/2021
 */

@HiltViewModel
class StopViewModel @Inject constructor(
    @Assisted stopId: Long
) : ViewModel() {

    val stopPages: StateFlow<List<StopPage>> = flowOf(listOf(
        StopPage(
            titleRes = R.string.stop_tab_eta,
            fragment = { StopEtaFragment() }
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