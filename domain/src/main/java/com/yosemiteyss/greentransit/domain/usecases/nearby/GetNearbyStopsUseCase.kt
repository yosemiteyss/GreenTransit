package com.yosemiteyss.greentransit.domain.usecases.nearby

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.models.distance
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

class GetNearbyStopsUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetNearbyStopsParams, List<NearbyStop>>(coroutineDispatcher) {

    private var lastCoordinate: Coordinate? = null

    override fun execute(parameters: GetNearbyStopsParams): Flow<Resource<List<NearbyStop>>> = flow {
        // Check if reached request distance
        val currentCoordinate = parameters.currentCoord

        if (lastCoordinate != null &&
            lastCoordinate!!.distance(currentCoordinate) > UPDATE_METER ||
            lastCoordinate == null
        ) {
            // Save new coordinate
            lastCoordinate = currentCoordinate

            coroutineScope {
                val deferred = parameters.bounds.map {
                    async { transitRepository.getNearbyStops(it.startHash, it.endHash) }
                }

                val distinctStops = deferred.awaitAll()
                    .flatten()
                    .distinctBy { it.id }

                emit(Resource.Success(distinctStops))
            }
        }
    }

    companion object {
        const val UPDATE_METER = 150
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