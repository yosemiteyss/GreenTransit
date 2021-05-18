package com.yosemiteyss.greentransit.app.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yosemiteyss.greentransit.app.utils.getOrAwaitValue
import com.yosemiteyss.greentransit.domain.repositories.FakeTrafficNewsRepositoryImpl
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class NewsViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    /*
    @Test
    fun `test used correct background color for status`() = coroutineRule.runBlockingTest {
        var viewModel = createNewsViewModel()
    }*/

    @Test
    fun test_if_it_has_data() {
        var viewModel = createNewsViewModel(false)
        val test = viewModel.trafficNews.getOrAwaitValue()
        test.forEachIndexed { i, item ->
            if (i == 0) assert(item is TrafficNewsListModel.TrafficNewsHeader)
            else assert(item is TrafficNewsListModel.TrafficNewsItem)
        }
    }

    @Test
    fun test_if_network_but_no_data() {
        var viewModel = createNewsViewModel(false, true)
        val test = viewModel.trafficNews.getOrAwaitValue()
        assertEquals(test.size, 1)
        assert(test.first() is TrafficNewsListModel.TrafficNewsHeader)
    }

    @Test
    fun test_if_network_fails() {
        var viewModel = createNewsViewModel(true)
        assertEquals(viewModel.trafficNews.getOrAwaitValue(5), listOf(TrafficNewsListModel.TrafficNewsEmptyItem))
    }

    private fun createNewsViewModel(
        throwNetworkError: Boolean = false,
        dontMakeData: Boolean = false
    ) : NewsViewModel {
        val newsRepo = FakeTrafficNewsRepositoryImpl()
        newsRepo.setNetworkError(throwNetworkError)
        newsRepo.setDontMakeData(dontMakeData)
        return NewsViewModel (
            newsRepository = newsRepo,
            coroutineDispatcher = TestCoroutineDispatcher()
        )
    }

}