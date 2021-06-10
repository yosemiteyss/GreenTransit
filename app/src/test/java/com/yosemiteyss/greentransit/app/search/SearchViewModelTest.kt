package com.yosemiteyss.greentransit.app.search

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.usecases.search.SearchRoutesUseCase
import com.yosemiteyss.greentransit.testshared.repositories.FakeTransitRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.utils.runBlockingTest
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 16/5/2021
 */

class SearchViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test input query, return success ui state`() = coroutineRule.runBlockingTest {
        val viewModel = createSearchViewModel()

        viewModel.searchUiState.test {
            assert(expectItem() is SearchUiState.Idle)

            viewModel.onUpdateQuery("hello")
            assert(expectItem() is SearchUiState.Loading)

            expectItem().let {
                assert(
                    it is SearchUiState.Success &&
                    it.data.size == SearchViewModel.NUM_OF_RESULTS
                )
            }
        }
    }

    @Test
    fun `test network error, return error ui state`() = coroutineRule.runBlockingTest {
        val viewModel = createSearchViewModel(networkError = true)

        viewModel.searchUiState.test {
            assert(expectItem() is SearchUiState.Idle)

            viewModel.onUpdateQuery("hello")
            assert(expectItem() is SearchUiState.Loading)

            assert(expectItem() is SearchUiState.Error)
        }
    }

    @Test
    fun `test clear query, return idle state`() = coroutineRule.runBlockingTest {
        val viewModel = createSearchViewModel()

        viewModel.searchUiState.test {
            assert(expectItem() is SearchUiState.Idle)

            viewModel.onUpdateQuery("hello")
            assert(expectItem() is SearchUiState.Loading)

            expectItem().let {
                assert(
                    it is SearchUiState.Success &&
                    it.data.size == SearchViewModel.NUM_OF_RESULTS
                )
            }

            viewModel.onUpdateQuery("")
            assert(expectItem() is SearchUiState.Idle)
        }
    }

    private fun createSearchViewModel(networkError: Boolean = false): SearchViewModel {
        val fakeTransitRepositoryImpl = FakeTransitRepositoryImpl().apply {
            if (networkError) setNetworkError(true)
        }

        return SearchViewModel(
            searchRoutesUseCase = SearchRoutesUseCase(
                transitRepository = fakeTransitRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            )
        )
    }
}