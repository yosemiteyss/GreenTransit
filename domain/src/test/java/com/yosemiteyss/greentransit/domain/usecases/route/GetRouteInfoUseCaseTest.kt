package com.yosemiteyss.greentransit.domain.usecases.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.random.Random

/**
 * Created by kevin on 19/5/2021
 */

class GetRouteInfoUseCaseTest {

    private lateinit var getRouteInfoUseCase: GetRouteInfoUseCase
    private lateinit var fakeTransitRepositoryImpl: FakeTransitRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        getRouteInfoUseCase = GetRouteInfoUseCase(
            transitRepository = fakeTransitRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test error when route region code or route id not provided`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val param = GetRouteInfoParameters()
        getRouteInfoUseCase(param).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }

    @Test
    fun `test get route info success by region code`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val routeRegionCode = RouteCode(
            code = UUID.randomUUID().toString(),
            region = Region.values().random()
        )
        val param = GetRouteInfoParameters(routeCode = routeRegionCode)

        getRouteInfoUseCase(param).test {
            assert(expectItem() is Resource.Loading)
            expectItem().let {
                assert(
                    it is Resource.Success &&
                    it.data == fakeTransitRepositoryImpl.fakeRouteInfoByRegionCode
                )
            }
            expectComplete()
        }
    }

    @Test
    fun `test get route info success by route id`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(false)

        val routeId = Random.nextLong()
        val param = GetRouteInfoParameters(routeId = routeId)

        getRouteInfoUseCase(param).test {
            assert(expectItem() is Resource.Loading)
            expectItem().let {
                assert(
                    it is Resource.Success &&
                    it.data == fakeTransitRepositoryImpl.fakeRouteInfoByRouteId
                )
            }

            expectComplete()
        }
    }

    @Test
    fun `test network error`() = coroutineRule.runBlockingTest {
        fakeTransitRepositoryImpl.setNetworkError(true)

        val routeId = Random.nextLong()
        val param = GetRouteInfoParameters(routeId = routeId)

        getRouteInfoUseCase(param).test {
            assert(expectItem() is Resource.Loading)
            assert(expectItem() is Resource.Error)
            expectComplete()
        }
    }
}