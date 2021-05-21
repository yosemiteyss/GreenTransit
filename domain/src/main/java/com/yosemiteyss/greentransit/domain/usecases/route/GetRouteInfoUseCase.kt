//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.route

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.models.RouteInfo
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRouteInfoUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<GetRouteInfoParameters, List<RouteInfo>>(coroutineDispatcher) {

    override fun execute(parameters: GetRouteInfoParameters): Flow<Resource<List<RouteInfo>>> = flow {
        val (routeCode, routeId) = parameters

        val routeInfo = when {
            routeCode != null -> transitRepository.getRouteInfos(routeCode)
            routeId != null -> transitRepository.getRouteInfos(routeId)
            else -> throw Exception("Need either route code or route id to get route info.")
        }

        emit(Resource.Success(routeInfo))
    }
}

data class GetRouteInfoParameters(
    val routeCode: RouteCode? = null,
    val routeId: Long? = null
)
