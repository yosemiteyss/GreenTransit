package com.yosemiteyss.greentransit.app.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: TrafficNewsRepository
) : ViewModel() {

    // TODO: LiveData<List<TrafficNews>>
    val trafficNews: LiveData<List<TrafficNews>> = liveData { emptyList<TrafficNews>() }
}