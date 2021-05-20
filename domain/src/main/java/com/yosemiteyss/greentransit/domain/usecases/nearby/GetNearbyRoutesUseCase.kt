package com.yosemiteyss.greentransit.domain.usecases.nearby

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.NearbyRoute
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 16/5/2021
 */

class GetNearbyRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<List<NearbyStop>, List<NearbyRoute>>(coroutineDispatcher) {

    override fun execute(parameters: List<NearbyStop>): Flow<Resource<List<NearbyRoute>>> = flow {
        if (parameters.isNotEmpty()) {
            // Get distinct nearby routes
            val routeIds = parameters.map { it.routeId }.distinct()
            val routes = transitRepository.getNearbyRoutes(routeIds)
                .sortedBy { it.code }

            emit(Resource.Success(routes))
        } else {
            emit(Resource.Success(emptyList<NearbyRoute>()))
        }
    }
}