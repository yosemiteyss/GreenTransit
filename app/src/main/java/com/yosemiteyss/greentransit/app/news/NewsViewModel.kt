package com.yosemiteyss.greentransit.app.news

import android.util.Log
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
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val newsRepository: TrafficNewsRepository
) : ViewModel() {

    private val _trafficNewsMutableList = MutableLiveData<List<TrafficNewsListModel>>(emptyList())
    val trafficNews: LiveData<List<TrafficNewsListModel>> = _trafficNewsMutableList
    init {
        viewModelScope.launch(coroutineDispatcher) {
            val newsListModel = mutableListOf<TrafficNewsListModel>()
            newsListModel.add(TrafficNewsListModel.TrafficNewsHeader)
            try {
                val fetchedTrafficNewsList = newsRepository.getTrafficNews()
                if (fetchedTrafficNewsList.isNotEmpty()) {
                    newsListModel.addAll(fetchedTrafficNewsList.map {
                        TrafficNewsListModel.TrafficNewsItem(it)
                    })
                    Log.d("tag", newsListModel.toString())
                }
            } catch (e: Exception) {
                newsListModel.add(TrafficNewsListModel.TrafficNewsEmptyItem)
            }

            _trafficNewsMutableList.postValue(newsListModel)
        }
    }
}