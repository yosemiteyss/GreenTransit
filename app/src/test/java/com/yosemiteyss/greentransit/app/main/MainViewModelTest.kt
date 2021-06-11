package com.yosemiteyss.greentransit.app.main

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.usecases.location.GetDeviceLocationUseCase
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyStopsUseCase
import com.yosemiteyss.greentransit.testshared.repositories.FakeLocationRepositoryImpl
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 12/5/2021
 */

class MainViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test enable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()

        mainViewModel.permissionGranted.test {
            assertFalse(expectItem())   // Default
            mainViewModel.onPermissionGranted(true)
            assertTrue(expectItem())
        }
    }

    @Test
    fun `test disable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()

        mainViewModel.permissionGranted.test {
            assertFalse(expectItem())   // Default
            mainViewModel.onPermissionGranted(false)
            expectNoEvents()            // No change due to stateflow
        }
    }

    @Test
    fun `test show toast message`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()
        val message = "message"

        mainViewModel.toastMessage.test {
            mainViewModel.onShowToastMessage(message)
            assertEquals(message, expectItem())
        }
    }

    @Test
    fun `test get nearby stops success, list populated`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()

        mainViewModel.nearbyStops.test {
            assert(expectItem().isNotEmpty())
        }
    }

    @Test
    fun `test get nearby stops error, list remains empty`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel(throwNetworkError = true)

        mainViewModel.nearbyStops.test {
            assert(expectItem().isEmpty())
        }
    }

    private fun createMainViewModel(throwNetworkError: Boolean = false): MainViewModel {
        return MainViewModel(
            getDeviceLocationUseCase = GetDeviceLocationUseCase(
                locationRepository = FakeLocationRepositoryImpl(),
                coroutineDispatcher = coroutineRule.testDispatcher
            ),
            getNearbyStopsUseCase = GetNearbyStopsUseCase(
                transitRepository = FakeTransitRepositoryImpl().apply {
                    setNetworkError(throwNetworkError)
                },
                coroutineDispatcher = coroutineRule.testDispatcher
            )
        )
    }
}