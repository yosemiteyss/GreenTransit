package com.yosemiteyss.greentransit.domain.usecases.route

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopInfo
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 19/5/2021
 */

class GetRouteStopInfosUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<List<Long>, List<StopInfo>>(coroutineDispatcher) {

    override fun execute(parameters: List<Long>): Flow<Resource<List<StopInfo>>> = flow {
        if (parameters.isNotEmpty()) {
            coroutineScope {
                // Get stop infos from stop ids
                val stopInfos = parameters.map {
                    async { transitRepository.getStopInfo(stopId = it) }
                }.awaitAll()

                emit(Resource.Success(stopInfos))
            }
        } else {
            emit(Resource.Success(emptyList<StopInfo>()))
        }
    }
}
