package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopRouteResult
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
 * Created by kevin on 17/5/2021
 */

class GetStopRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Long, List<StopRouteResult>>(coroutineDispatcher) {

    override fun execute(parameters: Long): Flow<Resource<List<StopRouteResult>>> = flow {
        val stopRoutes = transitRepository.getStopRoutes(stopId = parameters)
        val routeIds = stopRoutes.map { it.routeId }

        coroutineScope {
            // Fire distinct get route code requests (to reduce the total number of requests)
            val routeCodes = routeIds.distinct().map { routeId ->
                async {
                    Pair(routeId, transitRepository.getRouteCode(routeId))
                }
            }.awaitAll()

            // Map the result back to StopRouteWithCode
            val result = stopRoutes.map { stopRoute ->
                StopRouteResult(
                    stopRoute = stopRoute,
                    routeRegionCode = routeCodes.first { it.first == stopRoute.routeId }.second
                )
            }

            emit(Resource.Success(result))
        }
    }
}