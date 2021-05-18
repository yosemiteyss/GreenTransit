package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopEtaResult
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
) : FlowUseCase<GetStopEtaShiftsParameters, List<StopEtaResult>>(coroutineDispatcher) {

    override fun execute(
        parameters: GetStopEtaShiftsParameters
    ): Flow<Resource<List<StopEtaResult>>> = flow {
        while (true) {
            // Get eta shifts
            val etaShifts = transitRepository.getStopEtaShifts(stopId = parameters.stopId)
            val routeIds = etaShifts.map { it.routeId }.distinct()

            coroutineScope {
                // Get route code and route info
                val routeCodes = routeIds.map { routeId ->
                    async {
                        Pair(routeId, transitRepository.getRouteCode(routeId))
                    }
                }.awaitAll()

                val routeInfos = routeIds.map { routeId ->
                    async {
                        Pair(routeId, transitRepository.getRouteInfo(routeId))
                    }
                }.awaitAll()

                val results = etaShifts.sortedBy { it.etaMin }
                    .map { shift ->
                        StopEtaResult(
                            routeId = shift.routeId,
                            routeSeq = shift.routeSeq,
                            routeCode = routeCodes.first { it.first == shift.routeId }.second,
                            dest = routeInfos.first { it.first == shift.routeId }.second
                                .map { it.direction }
                                .flatten()
                                .first { it.routeSeq == shift.routeSeq }
                                .dest,
                            etaMin = shift.etaMin,
                            etaDate = shift.etaDate,
                            remarks = shift.remarks
                        )
                    }

                emit(Resource.Success(results))
            }

            delay(parameters.interval)
        }
    }
}

data class GetStopEtaShiftsParameters(
    val stopId: Long,
    val interval: Long
)

