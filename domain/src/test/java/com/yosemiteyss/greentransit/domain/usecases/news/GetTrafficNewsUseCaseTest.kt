package com.yosemiteyss.greentransit.domain.usecases.news

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTrafficNewsRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 7/6/2021
 */

class GetTrafficNewsUseCaseTest {

    private lateinit var getTrafficNewsUseCase: GetTrafficNewsUseCase
    private lateinit var fakeTrafficNewsRepositoryImpl: FakeTrafficNewsRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTrafficNewsRepositoryImpl = FakeTrafficNewsRepositoryImpl()
        getTrafficNewsUseCase = GetTrafficNewsUseCase(
            trafficNewsRepository = fakeTrafficNewsRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test is news sorted by date desc`() = coroutineRule.runBlockingTest {
        fakeTrafficNewsRepositoryImpl.setNetworkError(false)
        getTrafficNewsUseCase(Unit).test {
            assert(expectItem() is Resource.Loading)

            val expected = fakeTrafficNewsRepositoryImpl.fakeTrafficNews
                .sortedByDescending { it.referenceDate.time }
            val expectItem = expectItem()

            assert(expectItem is Resource.Success && expectItem.data == expected)
            expectComplete()
        }
    }

    @Test
    fun `test get news error`() = coroutineRule.runBlockingTest {
        fakeTrafficNewsRepositoryImpl.setNetworkError(true)

        getTrafficNewsUseCase(Unit).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }
}