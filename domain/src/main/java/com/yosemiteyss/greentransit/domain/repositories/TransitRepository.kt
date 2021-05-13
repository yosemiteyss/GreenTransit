package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.RouteInfo
import com.yosemiteyss.greentransit.domain.models.StopLocation

/**
 * Created by kevin on 12/5/2021
 */

interface TransitRepository {

    suspend fun getNearbyStops(startHash: String, endHash: String): List<StopLocation>

    suspend fun getRouteInfo(routeId: Long): RouteInfo
}
