package com.yosemiteyss.greentransit.domain.utils

import app.cash.turbine.test
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 4/19/21
 */

class NetworkCacheResourceTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Test
    fun `v1 - test network success, result is network data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkData() },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_NETWORK_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v1 - test network success, confirm cache updated`() = coroutineRule.runBlockingTest {
        var newCache: String? = null
        val ncr = networkCacheResource(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkData() },
            updateCache = { newCache = it }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_NETWORK_DATA)
            assert(newCache == FAKE_NETWORK_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v1 - test network error, result is cache data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkData(true) },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_CACHE_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v2 - test network success, result is network data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource2(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkDataFlow() },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_NETWORK_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v2 - test network success, confirm cache updated`() = coroutineRule.runBlockingTest {
        var newCache: String? = null

        val ncr = networkCacheResource2(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkDataFlow() },
            updateCache = { newCache = it },
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_NETWORK_DATA)
            assert(newCache == FAKE_NETWORK_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v2 - test network error, result is cache data`() = coroutineRule.runBlockingTest {
        val ncr = networkCacheResource2(
            cacheSource = { getFakeCacheData() },
            networkSource = { getFakeNetworkDataFlow(true) },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)
            val result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_CACHE_DATA)
            expectComplete()
        }
    }

    @Test
    fun `v2 - test network error, result is cache data, network still alive`() = coroutineRule.runBlockingTest {
        val networkFlow = MutableStateFlow<Resource<String>>(Resource.Loading())

        val ncr = networkCacheResource2(
            cacheSource = { getFakeCacheData() },
            networkSource = { networkFlow },
            updateCache = { }
        )

        ncr.test {
            assert(expectItem() is Resource.Loading)

            // Receive cache data
            networkFlow.emit(Resource.Error("Network error."))
            var result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_CACHE_DATA)

            // Network still alive, emit new network data
            networkFlow.emit(Resource.Success(FAKE_NETWORK_DATA))
            result = expectItem()
            assert(result is Resource.Success && result.data == FAKE_NETWORK_DATA)
        }
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun getFakeCacheData(): String = FAKE_CACHE_DATA

    @Suppress("RedundantSuspendModifier")
    private suspend fun getFakeNetworkData(hasNetworkError: Boolean = false): String {
        if (hasNetworkError) throw Exception("Network error.")
        return FAKE_NETWORK_DATA
    }

    private fun getFakeNetworkDataFlow(
        hasNetworkError: Boolean = false
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        if (hasNetworkError) {
            emit(Resource.Error("Network error."))
        } else {
            emit(Resource.Success(FAKE_NETWORK_DATA))
        }
    }

    companion object {
        private const val FAKE_CACHE_DATA = "cache data"
        private const val FAKE_NETWORK_DATA = "network data"
    }
}