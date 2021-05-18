package com.yosemiteyss.greentransit.domain.usecases.search

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 18/5/2021
 */

class SearchRoutesUseCaseTest {

    private lateinit var searchRoutesUseCase: SearchRoutesUseCase
    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        searchRoutesUseCase = SearchRoutesUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test query empty, return success empty list`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val param = SearchRoutesParameter(query = "\t\t\t   ", numOfRoutes = 4)

        searchRoutesUseCase(param).test {
            assert(expectItem() is Resource.Loading)

            val result = expectItem()
            assert(
                result is Resource.Success &&
                result.data.isEmpty()
            )

            expectComplete()
        }
    }

    @Test
    fun `test get routes success, return fixed num of routes`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val numOfRoutes = 4
        val param = SearchRoutesParameter(query = "abcd", numOfRoutes = numOfRoutes)

        searchRoutesUseCase(param).test {
            assert(expectItem() is Resource.Loading)

            val result = expectItem()
            assert(
                result is Resource.Success &&
                result.data.size == numOfRoutes
            )

            expectComplete()
        }
    }

    @Test
    fun `test get routes error`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(true)

        val param = SearchRoutesParameter(query = "abcd", numOfRoutes = 4)

        searchRoutesUseCase(param).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }
}