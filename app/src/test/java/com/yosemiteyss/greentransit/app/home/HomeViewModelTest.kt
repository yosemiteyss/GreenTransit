package com.yosemiteyss.greentransit.app.home

import android.location.Location
import app.cash.turbine.test
import com.yosemiteyss.greentransit.app.main.MainViewModel
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyRoutesUseCase
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyStopsUseCase
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import io.mockk.every
import io.mockk.mockk
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
    fun `test get nearby stops success, return not empty success ui state`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()
        val homeViewModel = createHomeViewModel()

        homeViewModel.getHomeUiState(mainViewModel.nearbyStops).test {
            assert(expectItem() is HomeUiState.Loading)

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            assert(expectItem() is HomeUiState.Success)
        }
    }

    @Test
    fun  `test get nearby stops error, return loading ui state`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel(throwNetworkError = true)
        val homeViewModel = createHomeViewModel()

        homeViewModel.getHomeUiState(mainViewModel.nearbyStops).test {
            assert(expectItem() is HomeUiState.Loading)

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            expectNoEvents()
        }
    }

    @Test
    fun `test get nearby stops success, get nearby routes error, return error ui state`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()
        val homeViewModel = createHomeViewModel(throwNetworkError = true)

        homeViewModel.getHomeUiState(mainViewModel.nearbyStops).test {
            assert(expectItem() is HomeUiState.Loading)

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            assert(expectItem() is HomeUiState.Error)
        }
    }

    @Test
    fun `test nearby routes count updated`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()
        val homeViewModel = createHomeViewModel()

        homeViewModel.getHomeUiState(mainViewModel.nearbyStops).test {
            assert(expectItem() is HomeUiState.Loading)

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            expectItem().let {
                assert(it is HomeUiState.Success && it.data.isNotEmpty())
            }
        }
    }

    private fun createMockLocation(
        latitude: Double,
        longitude: Double,
        bearing: Float? = null
    ): Location {
        val location = mockk<Location>()

        every { location.latitude } returns latitude
        every { location.longitude } returns longitude
        bearing?.let { every { location.bearing } returns it }

        return location
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

    private fun createMainViewModel(throwNetworkError: Boolean = false): MainViewModel {
        return MainViewModel(
            getNearbyStopsUseCase = GetNearbyStopsUseCase(
                transitRepository = FakeTransitRepositoryImpl().apply {
                    setNetworkError(throwNetworkError)
                },
                coroutineDispatcher = coroutineRule.testDispatcher
            )
        )
    }
}