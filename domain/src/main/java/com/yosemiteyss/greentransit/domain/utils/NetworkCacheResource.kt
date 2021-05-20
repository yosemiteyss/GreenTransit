package com.yosemiteyss.greentransit.domain.utils

import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kevin on 1/23/21
 */

inline fun <T> networkCacheResource(
    noinline cacheSource: suspend () -> T,
    crossinline networkSource: suspend () -> T,
    noinline updateCache: suspend (T) -> Unit
): Flow<Resource<T>> = channelFlow {
    channel.offer(Resource.Loading())

    try {
        val result = networkSource()
        channel.offer(Resource.Success(result))
        startUpdateCache(updateCache, result)
    } catch (e: Exception) {
        startFetchCacheSource(cacheSource, false)
    }
}

inline fun <T> networkCacheResource2(
    noinline cacheSource: suspend () -> T,
    crossinline networkSource: () -> Flow<Resource<T>>,
    noinline updateCache: suspend (T) -> Unit,
    keepNetworkAlive: Boolean = true
): Flow<Resource<T>> = channelFlow {
    var observeCacheJob: Job? = null
    networkSource().collect { resource ->
        when (resource) {
            is Resource.Success -> {
                // Cancel caching when the network is restored.
                observeCacheJob?.cancelIfActive()
                channel.offer(resource)
                startUpdateCache(updateCache, resource.data, keepNetworkAlive)
            }
            is Resource.Error -> {
                // Emit result from cache flow when the network is down.
                observeCacheJob = startFetchCacheSource(cacheSource, keepNetworkAlive)
            }
            is Resource.Loading -> {
                channel.offer(resource)
            }
        }
    }
}

@PublishedApi internal fun<T> ProducerScope<Resource<T>>.startFetchCacheSource(
    cacheSource: suspend () -> T,
    keepNetworkAlive: Boolean = false,
): Job = launch {
    try {
        val result = cacheSource()
        channel.offer(Resource.Success(result))
    } catch (e: Exception) {
        channel.offer(Resource.Error(e.message))
    }

    if (!keepNetworkAlive) channel.close()
}

@PublishedApi internal fun<T> ProducerScope<Resource<T>>.startUpdateCache(
    updateLocal: suspend (T) -> Unit,
    networkData: T,
    keepNetworkAlive: Boolean = false,
): Job = launch {
    updateLocal(networkData)
    if (!keepNetworkAlive) channel.close()
}