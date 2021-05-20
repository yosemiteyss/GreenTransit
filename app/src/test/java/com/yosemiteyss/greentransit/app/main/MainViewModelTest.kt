package com.yosemiteyss.greentransit.app.main

import android.location.Location
import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.usecases.nearby.GetNearbyStopsUseCase
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Created by kevin on 12/5/2021
 */

class MainViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test enable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()

        mainViewModel.mapEnabled.test {
            assertFalse(expectItem())   // Default
            mainViewModel.onEnableMap(true)
            assertTrue(expectItem())
        }
    }

    @Test
    fun `test disable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel()

        mainViewModel.mapEnabled.test {
            assertFalse(expectItem())   // Default
            mainViewModel.onEnableMap(false)
            expectNoEvents()            // No change due to stateflow
        }
    }

    /*
    // TODO: mocked location always return null
    @Test
    fun `test update location and bearing`() = coroutineRule.runBlockingTest {
        val latitude = 23.9
        val longitude = 11.2
        val bearing = 241.1.toFloat()

        val mockViewModel = spyk(createMainViewModel(), recordPrivateCalls = true)
        val mockLocation = createMockLocation(latitude, longitude)
        val mockLocationWithBearing = createMockLocation(latitude, longitude, bearing)

        every { mockViewModel invoke "buildLocation" withArguments listOf(bearing, mockLocation) } returns mockLocationWithBearing


        mockViewModel.userLocation.test {
            mockViewModel.onUpdateBearing(bearing)
            mockViewModel.onUpdateLocation(mockLocation)
            println(expectItem().latitude)
        }
    }

     */

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
            assertEquals(emptyList<NearbyStop>(), expectItem())

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            assert(expectItem().isNotEmpty())
        }
    }

    @Test
    fun `test get nearby stops error, list remains empty`() = coroutineRule.runBlockingTest {
        val mainViewModel = createMainViewModel(throwNetworkError = true)

        mainViewModel.nearbyStops.test {
            assertEquals(emptyList<NearbyStop>(), expectItem())

            mainViewModel.onUpdateLocation(createMockLocation(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble(),
                bearing = Random.nextFloat()
            ))

            expectNoEvents()
        }
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
}