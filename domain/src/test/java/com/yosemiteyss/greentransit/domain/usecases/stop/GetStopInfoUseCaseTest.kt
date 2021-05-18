package com.yosemiteyss.greentransit.domain.usecases.stop

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
 * Created by kevin on 18/5/2021
 */

class GetStopInfoUseCaseTest {

    private lateinit var getStopInfoUseCase: GetStopInfoUseCase
    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        getStopInfoUseCase = GetStopInfoUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test get stop info success`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val stopId = Random.nextLong()
        getStopInfoUseCase(stopId).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Success)
            expectComplete()
        }
    }

    @Test
    fun `test get stop info error`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(true)

        val stopId = Random.nextLong()
        getStopInfoUseCase(stopId).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }
}