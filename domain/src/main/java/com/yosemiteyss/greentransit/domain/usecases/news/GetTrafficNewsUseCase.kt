package com.yosemiteyss.greentransit.domain.usecases.news

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 7/6/2021
 */

class GetTrafficNewsUseCase @Inject constructor(
    private val trafficNewsRepository: TrafficNewsRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<TrafficNews>>(coroutineDispatcher) {

    override fun execute(parameters: Unit): Flow<Resource<List<TrafficNews>>> = flow {
        // Sort by the latest news
        val trafficNews = trafficNewsRepository.getTrafficNews()
            .sortedByDescending { it.referenceDate.time }

        emit(Resource.Success(trafficNews))
    }
}