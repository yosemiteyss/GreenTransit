package com.yosemiteyss.greentransit.domain.usecases.search

import androidx.paging.PagingData
import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.RouteRegion
import com.yosemiteyss.greentransit.domain.models.RouteRegionCode
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.usecases.PagingUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 16/5/2021
 */

class GetRegionRoutesUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : PagingUseCase<RouteRegion, RouteRegionCode>(coroutineDispatcher) {

    override fun execute(parameters: RouteRegion): Flow<PagingData<RouteRegionCode>> = flow {
        emitAll(transitRepository.getRegionRoutes(parameters))
    }
}