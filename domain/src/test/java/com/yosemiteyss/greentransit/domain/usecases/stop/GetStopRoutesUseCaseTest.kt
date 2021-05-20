package com.yosemiteyss.greentransit.domain.usecases.stop

import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 19/5/2021
 */

class GetStopRoutesUseCaseTest {

    private lateinit var getStopRoutesUseCase: GetStopRoutesUseCase
    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        getStopRoutesUseCase = GetStopRoutesUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test `() = coroutineRule.runBlockingTest {
        // input: stop id
        // return: routes of that stop


    }

    @Test
    fun `test network error`() = coroutineRule.runBlockingTest {

    }
}