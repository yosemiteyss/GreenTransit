//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases

import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Rule
import org.junit.Test

class CoroutineUseCaseTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test success resource`() = coroutineRule.runBlockingTest {
        val exampleCoroutineUseCase = ExampleCoroutineUseCase(coroutineRule.testDispatcher)
        val result = exampleCoroutineUseCase(Unit)
        assert(result is Resource.Success)
    }

    @Test
    fun `test error resource`() = coroutineRule.runBlockingTest {
        val exampleCoroutineUseCase = ExampleCoroutineUseCase(coroutineRule.testDispatcher, true)
        val result = exampleCoroutineUseCase(Unit)
        assert(result is Resource.Error)
    }

    private class ExampleCoroutineUseCase(
        coroutineDispatcher: CoroutineDispatcher,
        private val throwError: Boolean = false
    ) : CoroutineUseCase<Unit, Int>(coroutineDispatcher) {

        override suspend fun execute(parameters: Unit): Int {
            return if (throwError) {
                throw Exception("use case error.")
            } else {
                0
            }
        }
    }
}