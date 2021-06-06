//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.news.GetTrafficNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTrafficNewsUseCase: GetTrafficNewsUseCase
) : ViewModel() {

    private val _newsUiState = MutableLiveData<NewsUiState>(NewsUiState.Loading())
    val newsUiState: LiveData<NewsUiState> = _newsUiState

    init {
        loadTrafficNews()
    }

    fun loadTrafficNews(isSwipeRefresh: Boolean = false) = viewModelScope.launch {
        getTrafficNewsUseCase(Unit).collect { res ->
            _newsUiState.value = when (res) {
                is Resource.Success -> NewsUiState.Success(buildTrafficNewsListModels(res.data))
                is Resource.Error -> NewsUiState.Error(buildTrafficNewsListModels(), res.message)
                is Resource.Loading -> NewsUiState.Loading(isSwipeRefresh)
            }
        }
    }

    private fun buildTrafficNewsListModels(
        trafficNews: List<TrafficNews>? = null
    ): List<TrafficNewsListModel> {
        val newsListModel = mutableListOf<TrafficNewsListModel>()

        if (!trafficNews.isNullOrEmpty()) {
            newsListModel.add(TrafficNewsListModel.TrafficNewsHeader)
            newsListModel.addAll(trafficNews.map {
                TrafficNewsListModel.TrafficNewsItem(it)
            })
        } else {
            newsListModel.add(TrafficNewsListModel.TrafficNewsEmptyItem)
        }

        return newsListModel
    }
}

sealed class NewsUiState {
    data class Success(val data: List<TrafficNewsListModel>) : NewsUiState()
    data class Error(val data: List<TrafficNewsListModel>, val message: String?) : NewsUiState()
    data class Loading(val isSwipeRefresh: Boolean = false) : NewsUiState()
}