package com.yosemiteyss.greentransit.domain.usecases.route

import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `test results from the same route`() = coroutineRule.runBlockingTest {

    }

    @Test
    fun `test shift etas are latest ones`() = coroutineRule.runBlockingTest {

    }

    @Test
    fun `test shift etas are sorted by min ascending`() = coroutineRule.runBlockingTest {

    }

    @Test
    fun `test result size`() = coroutineRule.runBlockingTest {

    }

    @Test
    fun `test network error, coroutine still ongoing`() = coroutineRule.runBlockingTest {

    }

    private fun createGetRouteStopShiftEtasUseCase(): GetRouteStopShiftEtasUseCase {
        return GetRouteStopShiftEtasUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }
}