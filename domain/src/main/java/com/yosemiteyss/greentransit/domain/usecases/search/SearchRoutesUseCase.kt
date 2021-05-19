package com.yosemiteyss.greentransit.domain.usecases.search

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.RouteRegionCode
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

class SearchRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<SearchRoutesParameter, List<RouteRegionCode>>(coroutineDispatcher) {

    override fun execute(parameters: SearchRoutesParameter): Flow<Resource<List<RouteRegionCode>>> = flow {
        if (parameters.query.isNullOrBlank()) {
            emit(Resource.Success(emptyList<RouteRegionCode>()))
        } else {
            val routeCodes = transitRepository.searchRoute(
                query = parameters.query,
                numOfRoutes = parameters.numOfRoutes
            ).take(parameters.numOfRoutes)

            emit(Resource.Success(routeCodes))
        }
    }
}

data class SearchRoutesParameter(
    val query: String?,
    val numOfRoutes: Int
)