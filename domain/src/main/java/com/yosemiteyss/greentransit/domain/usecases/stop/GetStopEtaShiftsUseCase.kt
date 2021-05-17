package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopEtaShift
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kevin on 17/5/2021
 */

class GetStopEtaShiftsUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Long, List<StopEtaShift>>(coroutineDispatcher) {

    override fun execute(parameters: Long): Flow<Resource<List<StopEtaShift>>> = flow {
        val etaShifts = transitRepository.getStopEtaShifts(stopId = parameters)
        emit(Resource.Success(etaShifts))
    }
}