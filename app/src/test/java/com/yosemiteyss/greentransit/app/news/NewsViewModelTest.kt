package com.yosemiteyss.greentransit.app.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yosemiteyss.greentransit.app.utils.getOrAwaitValue
import com.yosemiteyss.greentransit.domain.repositories.FakeTrafficNewsRepositoryImpl
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NewsViewModelTest {

    @get:Rule
    var executorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun test_if_it_has_data() {
        val viewModel = createNewsViewModel()
        val test = viewModel.newsUiState.getOrAwaitValue()

        assert(test is NewsUiState.Success)
        val uiState = test as NewsUiState.Success

        uiState.data.forEachIndexed { i, item ->
            if (i == 0) assert(item is TrafficNewsListModel.TrafficNewsHeader)
            else assert(item is TrafficNewsListModel.TrafficNewsItem)
        }
    }

    @Test
    fun test_if_network_but_no_data() {
        val viewModel = createNewsViewModel(dontMakeData = true)
        val test = viewModel.newsUiState.getOrAwaitValue()

        assert(test is NewsUiState.Success)
        val uiState = test as NewsUiState.Success

        assertEquals(uiState.data.size, 1)
        assert(uiState.data.first() is TrafficNewsListModel.TrafficNewsHeader)
    }

    @Test
    fun test_if_network_fails() {
        val viewModel = createNewsViewModel(throwNetworkError = true)
        val test = viewModel.newsUiState.getOrAwaitValue()

        assert(test is NewsUiState.Error)
        val uiState = test as NewsUiState.Error

        assertEquals(
            listOf(TrafficNewsListModel.TrafficNewsEmptyItem),
            uiState.data
        )
    }

    private fun createNewsViewModel(
        throwNetworkError: Boolean = false,
        dontMakeData: Boolean = false
    ) : NewsViewModel {
        val newsRepo = FakeTrafficNewsRepositoryImpl().apply {
            setNetworkError(throwNetworkError)
            setDontMakeData(dontMakeData)
        }

        return NewsViewModel(
            newsRepository = newsRepo,
            coroutineDispatcher = coroutineRule.testDispatcher
        )
    }
}