package com.yosemiteyss.greentransit.app.search

import android.content.Context
import app.cash.turbine.test
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.domain.models.Region
import com.yosemiteyss.greentransit.domain.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.usecases.search.GetRegionRoutesUseCase
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 17/5/2021
 */

class RegionRoutesViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test used correct toolbar title for region`() = coroutineRule.runBlockingTest {
        var viewModel = createRegionRoutesViewModel(region = Region.KLN)

        viewModel.toolbarTitle.test {
            assertEquals(TITLE_REGION_KLN, expectItem())
        }

        viewModel = createRegionRoutesViewModel(region = Region.NT)

        viewModel.toolbarTitle.test {
            assertEquals(TITLE_REGION_NT, expectItem())
        }
    }

    @Test
    fun `test get route region success, return success ui state`() = coroutineRule.runBlockingTest {
        val region = Region.values().random()
        val viewModel = createRegionRoutesViewModel(region)

        viewModel.routesUiState.test {
            assert(expectItem() is RegionRoutesUiState.Success)
        }
    }

    @Test
    fun `test get route region failed, return error ui state`() = coroutineRule.runBlockingTest {
        val region = Region.values().random()
        val viewModel = createRegionRoutesViewModel(region, true)

        viewModel.routesUiState.test {
            assert(expectItem() is RegionRoutesUiState.Error)
        }
    }

    private fun createRegionRoutesViewModel(
        region: Region,
        networkError: Boolean = false
    ): RegionRoutesViewModel {
        val fakeTransitRepositoryImpl = FakeTransitRepositoryImpl().apply {
            if (networkError) setNetworkError(true)
        }

        return RegionRoutesViewModel(
            context = mockApplicationContext(),
            getRegionRoutesUseCase = GetRegionRoutesUseCase(
                transitRepository = fakeTransitRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            ),
            region = region
        )
    }

    private fun mockApplicationContext(): Context = mockk {
        every { getString(R.string.region_kln) } returns TITLE_REGION_KLN
        every { getString(R.string.region_hki) } returns TITLE_REGION_HKI
        every { getString(R.string.region_nt) } returns TITLE_REGION_NT
    }

    companion object {
        private const val TITLE_REGION_KLN = "Kowloon"
        private const val TITLE_REGION_HKI = "HK Island"
        private const val TITLE_REGION_NT = "New Territories"
    }
}