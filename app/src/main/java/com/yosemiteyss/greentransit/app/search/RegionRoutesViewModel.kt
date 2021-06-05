//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesEmptyModel
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesItemModel
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.search.GetRegionRoutesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegionRoutesViewModel @AssistedInject constructor(
    @ApplicationContext context: Context,
    getRegionRoutesUseCase: GetRegionRoutesUseCase,
    @Assisted region: Region
) : ViewModel() {

    private val _routesUiState = MutableStateFlow<RegionRoutesUiState>(RegionRoutesUiState.Loading)
    val routesUiState: StateFlow<RegionRoutesUiState> = _routesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getRegionRoutesUseCase(region).map { res ->
                when (res) {
                    is Resource.Success -> RegionRoutesUiState.Success(
                        buildRegionRoutesListModel(res.data)
                    )
                    is Resource.Error -> RegionRoutesUiState.Error(
                        data = listOf(RegionRoutesEmptyModel),
                        message = res.message
                    )
                    is Resource.Loading -> RegionRoutesUiState.Loading
                }
            }.collect { uiState ->
                _routesUiState.value = uiState
            }
        }
    }

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

    private fun buildRegionRoutesListModel(routeCodes: List<RouteCode>): List<RegionRoutesListModel> {
        return if (routeCodes.isEmpty()) {
            listOf(RegionRoutesEmptyModel)
        } else {
            routeCodes.map { RegionRoutesItemModel(it) }
        }
    }

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