package com.yosemiteyss.greentransit.domain.repositories

import androidx.paging.PagingData
import com.yosemiteyss.greentransit.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by kevin on 12/5/2021
 */

interface TransitRepository {

    suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop>

    suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute>

    suspend fun getRegionRoutes(region: RouteRegion): Flow<PagingData<RouteCode>>

    suspend fun getStopInfo(stopId: Long): StopInfo

    suspend fun getStopRoutes(stopId: Long): List<StopRoute>

    suspend fun getStopEtaShifts(stopId: Long): List<StopEtaShift>

    suspend fun getRouteCode(routeId: Long): RouteCode

    suspend fun getRouteInfo(routeId: Long): List<RouteInfo>

    suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode>
}
