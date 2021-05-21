//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases

import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    operator fun invoke(parameters: P): Flow<Resource<R>> = execute(parameters)
        .onStart { emit(Resource.Loading()) }
        .catch { e -> emit(Resource.Error(e.message)) }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<Resource<R>>
}