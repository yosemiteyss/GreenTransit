//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.search.SearchRoutesParameter
import com.yosemiteyss.greentransit.domain.usecases.search.SearchRoutesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchRoutesUseCase: SearchRoutesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    private val _searchUiState = MutableStateFlow<SearchUiState>(
        SearchUiState.Idle(buildSearchListModels())
    )
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    init {
        // Show regions
        viewModelScope.launch {
            _searchQuery.collect { query ->
                if (query.isNullOrBlank()) {
                    _searchUiState.value = SearchUiState.Idle(buildSearchListModels())
                }
            }
        }

        viewModelScope.launch {
           _searchQuery.debounce(SEARCH_RATE_LIMIT)
               .filterNot { it.isNullOrBlank() }
               .flatMapLatest { query ->
                    val params = SearchRoutesParameter(
                        query = query,
                        numOfRoutes = NUM_OF_RESULTS
                    )
                    searchRoutesUseCase(params)
                }
                .map { res ->
                    when (res) {
                        is Resource.Success -> {
                            if (res.data.isEmpty()) {
                                SearchUiState.Idle(buildSearchListModels())
                            } else {
                                SearchUiState.Success(buildSearchListModels(res.data))
                            }
                        }
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

    private fun buildSearchListModels(routeCodes: List<RouteCode>? = null): List<SearchListModel> {
        return routeCodes?.map {
            SearchListModel.SearchResultListModel(it)
        }
            ?: listOf(SearchListModel.SearchRegionsListModel)
    }

    companion object {
        const val NUM_OF_RESULTS = 5
        private const val SEARCH_RATE_LIMIT = 1000L
    }
}

sealed class SearchUiState {
    data class Success(val data: List<SearchListModel>) : SearchUiState()
    data class Error(val message: String?) : SearchUiState()
    data class Idle(val data: List<SearchListModel>) : SearchUiState()
    object Loading : SearchUiState()
}