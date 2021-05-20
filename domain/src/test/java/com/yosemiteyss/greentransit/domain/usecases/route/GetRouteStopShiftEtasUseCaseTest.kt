package com.yosemiteyss.greentransit.domain.usecases.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.launch
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Created by kevin on 19/5/2021
 */

class GetRouteStopShiftEtasUseCaseTest {

    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
    }

    @Test
    fun `test results count is the number of stops`() = coroutineRule.runBlockingTest {
        val param = createGetRouteStopShiftEtasParams()
        val useCase = createGetRouteStopShiftEtasUseCase()

        val job = coroutineRule.launch {
            useCase(param).test {
                assert(expectItem() is Resource.Loading)
                expectItem().let { actual ->
                    assert(
                        actual is Resource.Success &&
                        actual.data.size == fakeTransitRepositoryImpl.fakeRouteStops.size
                    )
                }
            }
        }

        job.cancel()
    }

    @Test
    fun `test shift etas are earliest ones`() = coroutineRule.runBlockingTest {
        val param = createGetRouteStopShiftEtasParams()
        val useCase = createGetRouteStopShiftEtasUseCase()

        val job = coroutineRule.launch {
            useCase(param).test {
                assert(expectItem() is Resource.Loading)

                val expected = fakeTransitRepositoryImpl.fakeRouteStopShiftEtas
                    .sortedBy { it.etaMin }
                    .firstOrNull()

                expectItem().let { actual ->
                    assert(
                        actual is Resource.Success &&
                        actual.data.all { it.routeStopShiftEta == expected }
                    )
                }
            }
        }

        job.cancel()
    }

    @Test
    fun `test network error`() = coroutineRule.runBlockingTest {
        val param = createGetRouteStopShiftEtasParams()
        val useCase = createGetRouteStopShiftEtasUseCase(true)

        val job = coroutineRule.launch {
            useCase(param).test {
                assert(expectItem() is Resource.Loading)
                assert(expectItem() is Resource.Error)
                expectComplete()
            }
        }

        job.cancel()
    }

    private fun createGetRouteStopShiftEtasUseCase(
        throwNetworkError: Boolean = false
    ): GetRouteStopShiftEtasUseCase {
        return GetRouteStopShiftEtasUseCase(
            transitRepository = fakeTransitRepositoryImpl.apply {
                setNetworkError(throwNetworkError)
            },
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    private fun createGetRouteStopShiftEtasParams(): GetRouteStopShiftEtasParameters {
        return GetRouteStopShiftEtasParameters(
            routeId = Random.nextLong(),
            routeSeq = Random.nextInt(),
            interval = 5000L
        )
    }
}