package com.yosemiteyss.greentransit.app.home

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyRoutesUseCase
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Created by kevin on 20/5/2021
 */

class HomeViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test map nearby stops to success ui state, not empty`() = coroutineRule.runBlockingTest {
        val homeViewModel = createHomeViewModel()
        val nearbyStops = MutableStateFlow(createFakeNearbyStops())

        homeViewModel.getHomeUiState(nearbyStops).test {
            println(expectItem())
        }
    }

    @Test
    fun `test map nearby stops to success ui state, is empty`() = coroutineRule.runBlockingTest {
        val nearbyStops = createFakeNearbyStops()
    }

    @Test
    fun `test map nearby stops to error ui state`() = coroutineRule.runBlockingTest {

    }

    @Test
    fun `test nearby routes count updated`() = coroutineRule.runBlockingTest {
        val homeViewModel = createHomeViewModel()
        val nearbyStops = MutableStateFlow(createFakeNearbyStops())


    }

    private fun createFakeNearbyStops(): List<NearbyStop> {
        return MutableList(10) {
            NearbyStop(
                id = Random.nextLong(),
                routeId = Random.nextLong(),
                location = Coordinate(
                    latitude = Random.nextDouble(),
                    longitude = Random.nextDouble()
                )
            )
        }
    }

    private fun createHomeViewModel(throwNetworkError: Boolean = false): HomeViewModel {
        return HomeViewModel(
            getNearbyRoutesUseCase = GetNearbyRoutesUseCase(
                transitRepository = FakeTransitRepositoryImpl().apply {
                    setNetworkError(throwNetworkError)
                },
                coroutineDispatcher = coroutineRule.testDispatcher
            )
        )
    }
}