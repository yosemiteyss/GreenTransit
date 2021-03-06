//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.utils

import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

inline fun <T> networkCacheResource(
    noinline cacheSource: suspend () -> T,
    crossinline networkSource: suspend () -> T,
    noinline updateCache: suspend (T) -> Unit
): Flow<Resource<T>> = channelFlow {
    channel.trySend(Resource.Loading())

    try {
        // Update cache when network request is successful.
        val result = networkSource()
        channel.trySend(Resource.Success(result))
        startUpdateCache(updateCache, result)
    } catch (e: Exception) {
        // Use local cache if network request is failed.
        startFetchCacheSource(cacheSource)
    }
}

@PublishedApi internal fun<T> ProducerScope<Resource<T>>.startFetchCacheSource(
    cacheSource: suspend () -> T
): Job = launch {
    try {
        val result = cacheSource()
        channel.trySend(Resource.Success(result))
    } catch (e: Exception) {
        channel.trySend(Resource.Error(e.message))
    }

    // Close channel when finished
    channel.close()
}

@PublishedApi internal fun<T> ProducerScope<Resource<T>>.startUpdateCache(
    updateLocal: suspend (T) -> Unit,
    networkData: T
): Job = launch {
    updateLocal(networkData)

    // Close channel when finished
    channel.close()
}