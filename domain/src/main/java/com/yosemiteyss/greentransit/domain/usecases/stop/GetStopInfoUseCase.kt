//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.di.IoDispatcher
import com.yosemiteyss.greentransit.domain.models.StopInfo
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStopInfoUseCase @Inject constructor(
    private val transitRepository: TransitRepository,
    @IoDispatcher coroutineDispatcher: CoroutineDispatcher
) : FlowUseCase<Long, StopInfo>(coroutineDispatcher) {

    override fun execute(parameters: Long): Flow<Resource<StopInfo>> = flow {
        val stopInfo = transitRepository.getStopInfo(stopId = parameters)
        emit(Resource.Success(stopInfo))
    }
}