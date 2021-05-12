package com.yosemiteyss.greentransit.domain.usecases.home

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.distance
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

private const val UPDATE_METER = 150

class GetNearbyStopsUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetNearbyStopsParams, List<Coordinate>>(coroutineDispatcher) {

    private var lastCoordinate: Coordinate? = null

    override fun execute(parameters: GetNearbyStopsParams): Flow<Resource<List<Coordinate>>> = flow {
        // Check if reached request distance
        val currentCoordinate = parameters.currentCoord

        if (lastCoordinate != null &&
            lastCoordinate!!.distance(currentCoordinate) > UPDATE_METER ||
            lastCoordinate == null
        ) {
            lastCoordinate = currentCoordinate

            val distinctStopsList = parameters.bounds
                .map { transitRepository.getNearbyStops(it.startHash, it.endHash) }
                .flatten()
                .distinctBy { it.stopId }
                .map { it.location }

            println("GetNearbyStopsUseCase")

            emit(Resource.Success(distinctStopsList))
        }
    }
}

data class GetNearbyStopsParams(
    val currentCoord: Coordinate,
    val bounds: List<NearbyGeoBound>
)

data class NearbyGeoBound(
    val startHash: String,
    val endHash: String
)