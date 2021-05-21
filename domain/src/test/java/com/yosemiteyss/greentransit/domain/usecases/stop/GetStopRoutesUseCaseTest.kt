//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.stop

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class GetStopRoutesUseCaseTest {

    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test results count is the number of stop routes`() = coroutineRule.runBlockingTest {
        val stopId = Random.nextLong()
        val useCase = createGetStopRoutesUseCase()

        useCase(stopId).test {
            assert(expectItem() is Resource.Loading)
            expectItem().let {
                assert(
                    it is Resource.Success &&
                    it.data.size == fakeTransitRepositoryImpl.fakeStopRoutes.size
                )
            }
            expectComplete()
        }
    }

    @Test
    fun `test network error`() = coroutineRule.runBlockingTest {
        val stopId = Random.nextLong()
        val useCase = createGetStopRoutesUseCase(true)

        useCase(stopId).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }

    private fun createGetStopRoutesUseCase(throwNetworkError: Boolean = false): GetStopRoutesUseCase {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl().apply {
            setNetworkError(throwNetworkError)
        }

        return GetStopRoutesUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }
}