package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopEtaShiftWithCode
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 17/5/2021
 */

class GetStopEtaShiftsUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetStopEtaShiftsParameters, List<StopEtaShiftWithCode>>(coroutineDispatcher) {

    override fun execute(
        parameters: GetStopEtaShiftsParameters
    ): Flow<Resource<List<StopEtaShiftWithCode>>> = flow {
        while (true) {
            println("GetStopEtaShiftsUseCase")
            val etaShifts = transitRepository.getStopEtaShifts(stopId = parameters.stopId)
            val routeIds = etaShifts.map { it.routeId }

            coroutineScope {
                val routeCodes = routeIds.distinct().map { routeId ->
                    async {
                        Pair(routeId, transitRepository.getRouteCode(routeId))
                    }
                }.awaitAll()

                val result = etaShifts.sortedBy { it.etaMin }
                    .map { etaShift ->
                        StopEtaShiftWithCode(
                            etaShift = etaShift,
                            routeCode = routeCodes.first { it.first == etaShift.routeId }.second
                        )
                    }

                emit(Resource.Success(result))
            }

            delay(parameters.interval)
        }
    }
}

data class GetStopEtaShiftsParameters(
    val stopId: Long,
    val interval: Long
)