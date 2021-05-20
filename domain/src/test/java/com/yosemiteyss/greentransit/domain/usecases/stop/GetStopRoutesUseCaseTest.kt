package com.yosemiteyss.greentransit.domain.usecases.stop

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Created by kevin on 19/5/2021
 */

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