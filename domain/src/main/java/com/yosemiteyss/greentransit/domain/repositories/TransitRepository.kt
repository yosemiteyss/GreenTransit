package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.NearbyRoute
import com.yosemiteyss.greentransit.domain.models.NearbyStop
import com.yosemiteyss.greentransit.domain.models.RouteCode
import com.yosemiteyss.greentransit.domain.models.StopEtaShift

/**
 * Created by kevin on 12/5/2021
 */

interface TransitRepository {

    suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop>

    suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute>

    suspend fun getStopEtaShiftList(stopId: Long): List<StopEtaShift>

    suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode>
}
