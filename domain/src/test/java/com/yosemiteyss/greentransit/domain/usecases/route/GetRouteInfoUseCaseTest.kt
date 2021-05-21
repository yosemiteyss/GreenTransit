//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.usecases.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.random.Random

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