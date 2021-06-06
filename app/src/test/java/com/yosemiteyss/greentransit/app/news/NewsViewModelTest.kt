package com.yosemiteyss.greentransit.app.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yosemiteyss.greentransit.app.utils.getOrAwaitValue
import com.yosemiteyss.greentransit.domain.usecases.news.GetTrafficNewsUseCase
import com.yosemiteyss.greentransit.testshared.repositories.FakeTrafficNewsRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NewsViewModelTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test success, header and items populated`() {
        val viewModel = createNewsViewModel()
        val uiState = viewModel.newsUiState.getOrAwaitValue()

        assert(uiState is NewsUiState.Success)
        uiState as NewsUiState.Success

        uiState.data.forEachIndexed { i, item ->
            if (i == 0) assert(item is TrafficNewsListModel.TrafficNewsHeader)
            else assert(item is TrafficNewsListModel.TrafficNewsItem)
        }
    }

    @Test
    fun `test empty, empty item populated`() {
        val viewModel = createNewsViewModel(dontMakeData = true)
        val uiState = viewModel.newsUiState.getOrAwaitValue()

        assert(uiState is NewsUiState.Success)
        uiState as NewsUiState.Success

        assertEquals(uiState.data.size, 1)
        assert(uiState.data.first() is TrafficNewsListModel.TrafficNewsEmptyItem)
    }

    @Test
    fun `test error, empty item populated`() {
        val viewModel = createNewsViewModel(throwNetworkError = true)
        val uiState = viewModel.newsUiState.getOrAwaitValue()

        assert(uiState is NewsUiState.Error)
        uiState as NewsUiState.Error

        assertEquals(
            listOf(TrafficNewsListModel.TrafficNewsEmptyItem),
            uiState.data
        )
    }

    private fun createNewsViewModel(
        throwNetworkError: Boolean = false,
        dontMakeData: Boolean = false
    ) : NewsViewModel {
        val fakeTrafficNewsRepositoryImpl = FakeTrafficNewsRepositoryImpl().apply {
            setNetworkError(throwNetworkError)
            setDontMakeData(dontMakeData)
        }

        return NewsViewModel(
            getTrafficNewsUseCase = GetTrafficNewsUseCase(
                trafficNewsRepository = fakeTrafficNewsRepositoryImpl,
                coroutineDispatcher = coroutineRule.testDispatcher
            )
        )
    }
}