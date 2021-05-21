//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.search

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<SearchRoutesParameter, List<RouteCode>>(coroutineDispatcher) {

    override fun execute(parameters: SearchRoutesParameter): Flow<Resource<List<RouteCode>>> = flow {
        if (parameters.query.isNullOrBlank()) {
            emit(Resource.Success(emptyList<RouteCode>()))
        } else {
            val routeCodes = transitRepository.searchRoute(
                query = parameters.query,
                numOfRoutes = parameters.numOfRoutes
            )
                .take(parameters.numOfRoutes)

            emit(Resource.Success(routeCodes))
        }
    }
}

data class SearchRoutesParameter(
    val query: String?,
    val numOfRoutes: Int
)