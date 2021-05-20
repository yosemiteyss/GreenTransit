package com.yosemiteyss.greentransit.domain.usecases.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Created by kevin on 19/5/2021
 */

class GetRouteStopInfosUseCaseTest {

    private lateinit var getRouteStopInfosUseCase: GetRouteStopInfosUseCase
    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        getRouteStopInfosUseCase = GetRouteStopInfosUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test input empty stop ids list, return empty list`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val stopIds = emptyList<Long>()
        getRouteStopInfosUseCase(stopIds).test {
            assert(expectItem() is Resource.Loading)

            expectItem().let {
                assert(it is Resource.Success && it.data.isEmpty())
            }

            expectComplete()
        }
    }

    @Test
    fun `test input n stop ids, return n stop infos`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val expectedSize = 10
        val stopIds = MutableList(expectedSize) { Random.nextLong() }

        getRouteStopInfosUseCase(stopIds).test {
            assert(expectItem() is Resource.Loading)

            expectItem().let {
                assert(it is Resource.Success && it.data.size == expectedSize)
            }

            expectComplete()
        }
    }

    @Test
    fun `test network error`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(true)

        val stopIds = MutableList(10) { Random.nextLong() }

        getRouteStopInfosUseCase(stopIds).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }
}