package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.RouteInfo
import com.yosemiteyss.greentransit.domain.models.RouteRegion
import com.yosemiteyss.greentransit.domain.models.StopLocation
import java.util.*

/**
 * Created by kevin on 12/5/2021
 */

class FakeTransitRepositoryImpl : TransitRepository {

    private var throwNetworkError = false

    val fakeStops: List<StopLocation> = MutableList(5) {
        createFakeStopLocation()
    }

    val fakeRouteInfo: RouteInfo = RouteInfo(
        id = Random().nextLong(),
        seq = Random().nextInt(),
        code = UUID.randomUUID().toString(),
        region = RouteRegion.values().random()
    )

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<StopLocation> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStops + fakeStops
    }

    override suspend fun getRouteInfo(routeId: Long): RouteInfo {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteInfo
    }

    fun setNetworkError(throwError: Boolean) {
        this.throwNetworkError = throwError
    }

    private fun createFakeStopLocation(): StopLocation {
        val random = Random()
        return StopLocation(
            stopId = random.nextLong(),
            location = Coordinate(
                latitude = random.nextDouble(),
                longitude = random.nextDouble()
            )
        )
    }
}