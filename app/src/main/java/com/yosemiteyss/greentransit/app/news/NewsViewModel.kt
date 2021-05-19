package com.yosemiteyss.greentransit.app.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: TrafficNewsRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _newsUiState = MutableLiveData<NewsUiState>(NewsUiState.Loading)
    val newsUiState: LiveData<NewsUiState> = _newsUiState

    init {
        viewModelScope.launch(coroutineDispatcher) {
            val newsListModel = mutableListOf<TrafficNewsListModel>()

            try {
                val fetchedTrafficNewsList = newsRepository.getTrafficNews()
                newsListModel.add(TrafficNewsListModel.TrafficNewsHeader)

                if (fetchedTrafficNewsList.isNotEmpty()) {
                    newsListModel.addAll(fetchedTrafficNewsList.map {
                        TrafficNewsListModel.TrafficNewsItem(it)
                    })
                }

                _newsUiState.postValue(NewsUiState.Success(newsListModel))
            } catch (e: Exception) {
                newsListModel.add(TrafficNewsListModel.TrafficNewsEmptyItem)

                _newsUiState.postValue(
                    NewsUiState.Error(newsListModel, e.message)
                )
            }
        }
    }
}

sealed class NewsUiState {
    data class Success(val data: List<TrafficNewsListModel>) : NewsUiState()
    data class Error(val data: List<TrafficNewsListModel>, val message: String?) : NewsUiState()
    object Loading : NewsUiState()
}