//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases

import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import org.junit.Rule
import org.junit.Test

class FlowUseCaseTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test success resource`() = coroutineRule.runBlockingTest {
        val exampleFlowUseCase = ExampleFlowUseCase(coroutineRule.testDispatcher)
        val results = exampleFlowUseCase(Unit).toList()
        assert(results[0] is Resource.Loading)
        assert(results[1] is Resource.Success)
    }

    @Test
    fun `test error resource`() = coroutineRule.runBlockingTest {
        val exampleFlowUseCase = ExampleFlowUseCase(coroutineRule.testDispatcher, true)
        val results = exampleFlowUseCase(Unit).toList()
        assert(results[0] is Resource.Loading)
        assert(results[1] is Resource.Error)
    }

    private class ExampleFlowUseCase(
        coroutineDispatcher: CoroutineDispatcher,
        private val throwError: Boolean = false
    ) : FlowUseCase<Unit, Unit>(coroutineDispatcher) {

        override fun execute(parameters: Unit): Flow<Resource<Unit>> = flow {
            if (throwError) {
                throw Exception("auth error.")
            } else {
                emit(Resource.Success(Unit))
            }
        }
    }
}