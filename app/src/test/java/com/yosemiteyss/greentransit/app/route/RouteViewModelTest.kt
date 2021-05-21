package com.yosemiteyss.greentransit.app.route

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.usecases.route.GetRouteInfoUseCase
import com.yosemiteyss.greentransit.domain.usecases.route.GetRouteStopInfosUseCase
import com.yosemiteyss.greentransit.domain.usecases.route.GetRouteStopShiftEtasUseCase
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Created by kevin on 20/5/2021
 */

class RouteViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test get route info success`() = coroutineRule.runBlockingTest {
        val routeOption = RouteOption(
            routeRegionCode = RouteCode(
                code = UUID.randomUUID().toString(),
                region = Region.values().random()
            )
        )
        val viewModel = createRouteViewModel(routeOption)

       viewModel.routeInfos.test {
           assert(expectItem() is Resource.Success)
           cancelAndIgnoreRemainingEvents()
       }
    }

    @Test
    fun `test update current direction`() = coroutineRule.runBlockingTest {
        val routeOption = RouteOption(
            routeRegionCode = RouteCode(
                code = UUID.randomUUID().toString(),
                region = Region.values().random()
            )
        )
        val viewModel = createRouteViewModel(routeOption)

        viewModel.currentDirection.test {
            assertNotNull(expectItem())
        }
    }

    @Test
    fun `test update stop list models success`() = coroutineRule.runBlockingTest {
        val routeOption = RouteOption(
            routeRegionCode = RouteCode(
                code = UUID.randomUUID().toString(),
                region = Region.values().random()
            )
        )
        val viewModel = createRouteViewModel(routeOption)

        viewModel.stopsListModels.test {
            assert(expectItem() is Resource.Success)
        }
    }

    private fun createRouteViewModel(
        routeOption: RouteOption,
        throwNetworkError: Boolean = false
    ): RouteViewModel {
        val fakeTransitRepositoryImpl = FakeTransitRepositoryImpl()
        fakeTransitRepositoryImpl.setNetworkError(throwNetworkError)

        return RouteViewModel(
            getRouteInfoUseCase = GetRouteInfoUseCase(
                transitRepository = fakeTransitRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            ),
            getRouteStopShiftEtasUseCase = GetRouteStopShiftEtasUseCase(
                transitRepository = fakeTransitRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            ),
            getRouteStopInfosUseCase = GetRouteStopInfosUseCase(
                transitRepository = fakeTransitRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            ),
            routeOption = routeOption
        )
    }
}