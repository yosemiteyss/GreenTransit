package com.yosemiteyss.greentransit.domain.usecases.location

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.models.Location
import com.yosemiteyss.greentransit.testshared.repositories.FakeLocationRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 6/6/2021
 */

class GetDeviceLocationUseCaseTest {

    private lateinit var getDeviceLocationUseCase: GetDeviceLocationUseCase
    private lateinit var fakeLocationRepositoryImpl: FakeLocationRepositoryImpl

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {
        fakeLocationRepositoryImpl = FakeLocationRepositoryImpl()
        getDeviceLocationUseCase = GetDeviceLocationUseCase(
            locationRepository = fakeLocationRepositoryImpl,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }

    @Test
    fun `test coordinate and orientation combined`() = coroutineRule.runBlockingTest {
        getDeviceLocationUseCase().test {
            val expected = Location(
                coordinate = fakeLocationRepositoryImpl.fakeLocation,
                orientation = fakeLocationRepositoryImpl.fakeOrientation
            )

            assertEquals(expected, expectItem())
        }
    }
}