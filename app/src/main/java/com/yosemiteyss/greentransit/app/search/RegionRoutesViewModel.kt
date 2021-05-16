package com.yosemiteyss.greentransit.app.search

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.*
import com.yosemiteyss.greentransit.domain.models.RouteRegion
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
    @Assisted routeRegion: RouteRegion
) : ViewModel() {

    val regionRoutes: Flow<PagingData<RegionRoutesListModel>> = getRegionRoutesUseCase(routeRegion)
        .map { pagingData ->
            pagingData.map { RegionRoutesItemModel(it) }
        }
        .map { pagingData ->
            pagingData.insertSeparators { before, after ->
                return@insertSeparators if (before == null && after == null)
                    RegionRoutesEmptyModel else
                    null
            }
        }
        .cachedIn(viewModelScope)

    val toolbarTitle: StateFlow<String> = flowOf(routeRegion)
        .map { region ->
            when (region) {
               RouteRegion.KLN -> context.getString(R.string.region_kln)
               RouteRegion.HKI -> context.getString(R.string.region_hki)
               RouteRegion.NT -> context.getString(R.string.region_nt)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

    @dagger.assisted.AssistedFactory
    interface RegionRoutesViewModelFactory {
        fun create(routeRegion: RouteRegion): RegionRoutesViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: RegionRoutesViewModelFactory,
            routeRegion: RouteRegion
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(routeRegion) as T
            }
        }
    }
}