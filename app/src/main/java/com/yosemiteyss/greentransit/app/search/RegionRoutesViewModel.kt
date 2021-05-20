package com.yosemiteyss.greentransit.app.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesEmptyModel
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesItemModel
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.search.GetRegionRoutesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*

/**
 * Created by kevin on 16/5/2021
 */

class RegionRoutesViewModel @AssistedInject constructor(
    @ApplicationContext context: Context,
    getRegionRoutesUseCase: GetRegionRoutesUseCase,
    @Assisted region: Region
) : ViewModel() {

    val routesUiState: StateFlow<RegionRoutesUiState> = getRegionRoutesUseCase(region)
        .map { res ->
            when (res) {
                is Resource.Success -> {
                    val listModels = if (res.data.isEmpty())
                        listOf(RegionRoutesEmptyModel) else
                        res.data.map { RegionRoutesItemModel(it) }

                    RegionRoutesUiState.Success(data = listModels)
                }
                is Resource.Error -> RegionRoutesUiState.Error(
                    data = listOf(RegionRoutesEmptyModel),
                    message = res.message
                )
                is Resource.Loading -> RegionRoutesUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RegionRoutesUiState.Loading
        )

    val toolbarTitle: StateFlow<String> = flowOf(region)
        .map { region ->
            when (region) {
               Region.KLN -> context.getString(R.string.region_kln)
               Region.HKI -> context.getString(R.string.region_hki)
               Region.NT -> context.getString(R.string.region_nt)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

    @dagger.assisted.AssistedFactory
    interface RegionRoutesViewModelFactory {
        fun create(region: Region): RegionRoutesViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RegionRoutesViewModelFactory,
            region: Region
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(region) as T
            }
        }
    }
}

sealed class RegionRoutesUiState {
    data class Success(val data: List<RegionRoutesListModel>) : RegionRoutesUiState()
    data class Error(val data: List<RegionRoutesListModel>, val message: String?) : RegionRoutesUiState()
    object Loading : RegionRoutesUiState()
}