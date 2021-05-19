package com.yosemiteyss.greentransit.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.models.RouteRegionCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.search.SearchRoutesParameter
import com.yosemiteyss.greentransit.domain.usecases.search.SearchRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchRoutesUseCase: SearchRoutesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    private val _searchUiState = MutableStateFlow<SearchUiState?>(null)
    val searchUiState: StateFlow<SearchUiState?> = _searchUiState.asStateFlow()

    init {
        viewModelScope.launch {
           _searchQuery.debounce(SEARCH_RATE_LIMIT)
                .flatMapLatest { query ->
                    val params = SearchRoutesParameter(
                        query = query,
                        numOfRoutes = NUM_OF_RESULTS
                    )

                    searchRoutesUseCase(params)
                }
                .map { res ->
                    when (res) {
                        is Resource.Success -> SearchUiState.Success(buildSearchListModels(res.data))
                        is Resource.Error -> SearchUiState.Error(res.message)
                        is Resource.Loading -> SearchUiState.Loading
                    }
                }
               .collect {
                   _searchUiState.value = it
               }
        }
    }

    fun onUpdateQuery(query: String?) {
        _searchQuery.value = query
    }

    private fun buildSearchListModels(routeRegionCodes: List<RouteRegionCode>): List<SearchListModel> {
        return routeRegionCodes.map {
            SearchListModel.SearchResultListModel(it)
        }
    }

    companion object {
        const val NUM_OF_RESULTS = 5
        private const val SEARCH_RATE_LIMIT = 1000L
    }
}

sealed class SearchUiState {
    data class Success(val data: List<SearchListModel>) : SearchUiState()
    data class Error(val message: String?) : SearchUiState()
    object Loading : SearchUiState()
}