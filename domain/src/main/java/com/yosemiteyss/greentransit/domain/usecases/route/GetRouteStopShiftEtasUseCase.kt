package com.yosemiteyss.greentransit.domain.usecases.route

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.RouteStopShiftEtaResult
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 18/5/2021
 */

class GetRouteStopShiftEtasUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetRouteStopShiftEtasParameters, List<RouteStopShiftEtaResult>>(coroutineDispatcher) {

    override fun execute(
        parameters: GetRouteStopShiftEtasParameters
    ): Flow<Resource<List<RouteStopShiftEtaResult>>> = flow {
        while (true) {
            // Get route stops' info
            val routeStops = transitRepository.getRouteStops(
                routeId = parameters.routeId,
                routeSeq = parameters.routeSeq
            )

            // Get route stops' etas by stop ids
            coroutineScope {
                val shiftEtas = routeStops.map { stop ->
                    async {
                        // Get the shift etas of all stops of the route
                        transitRepository.getRouteStopShiftEtas(
                            routeId = parameters.routeId,
                            stopId = stop.stopId
                        ).sortedBy {
                            it.etaMin
                        }
                    }
                }.awaitAll()

                // Combine results, use the earliest shift eta
                val results = routeStops.mapIndexed { index, routeStop ->
                    RouteStopShiftEtaResult(
                        routeStop = routeStop,
                        routeStopShiftEta = shiftEtas[index].firstOrNull()
                    )
                }

                emit(Resource.Success(results))
            }

            delay(parameters.interval)
        }
    }
}

data class GetRouteStopShiftEtasParameters(
    val routeId: Long,
    val routeSeq: Int,
    val interval: Long
)