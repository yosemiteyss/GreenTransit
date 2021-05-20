package com.yosemiteyss.greentransit.domain.utils

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import kotlinx.coroutines.flow.toList
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 5/20/21
 */

class NetworkCacheResourceTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `test loading emitted at the beginning`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource(
            cacheSource = { getCacheData() },
            networkSource = { getNetworkData() },
            updateCache = { }
        )

        val first = ncr.toList().first()

        assert(first is Resource.Loading)
    }

    @Test
    fun `test network success, result is network data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource(
            cacheSource = { getCacheData() },
            networkSource = { getNetworkData() },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == NETWORK_DATA)
            expectComplete()
        }
    }

    @Test
    fun `test network error, result is cache data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource(
            cacheSource = { getCacheData() },
            networkSource = { getNetworkData(true) },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == CACHE_DATA)
            expectComplete()
        }
    }

    @Test
    fun `test network success, confirm cache updated`() = coroutineRule.runBlockingTest {
        var cache: String? = null

        val ncr = networkCacheResource(
            cacheSource = { getCacheData() },
            networkSource = { getNetworkData() },
            updateCache = { cache = it }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == NETWORK_DATA)
            assert(cache == NETWORK_DATA)
            expectComplete()
        }
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun getCacheData(): String = CACHE_DATA

    @Suppress("RedundantSuspendModifier")
    private suspend fun getNetworkData(hasNetworkError: Boolean = false): String {
        if (hasNetworkError) throw Exception("Network error.")
        return NETWORK_DATA
    }

    companion object {
        private const val CACHE_DATA = "cache_data"
        private const val NETWORK_DATA = "network_data"
    }
}