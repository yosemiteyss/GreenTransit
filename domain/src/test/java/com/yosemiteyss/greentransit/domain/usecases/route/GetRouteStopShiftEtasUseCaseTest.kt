//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.launch
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

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