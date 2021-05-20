package com.yosemiteyss.greentransit.domain.usecases.search

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by kevin on 16/5/2021
 */

class GetRegionRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Region, List<RouteCode>>(coroutineDispatcher) {

    override fun execute(parameters: Region): Flow<Resource<List<RouteCode>>> {
        return transitRepository.getRegionRoutes(region = parameters)
            .map { res ->
                when (res) {
                    is Resource.Success -> Resource.Success(res.data.sortedBy { it.code })
                    is Resource.Error -> Resource.Error(res.message)
                    is Resource.Loading -> Resource.Loading()
                }
            }
    }
}