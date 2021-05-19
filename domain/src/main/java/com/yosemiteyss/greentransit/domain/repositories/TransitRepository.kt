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

    suspend fun getRegionRoutes(region: RouteRegion): Flow<PagingData<RouteRegionCode>>

    suspend fun getStopInfo(stopId: Long): StopInfo

    suspend fun getStopRoutes(stopId: Long): List<StopRoute>

    suspend fun getStopRouteShiftEtas(stopId: Long): List<StopRouteShiftEta>

    suspend fun getRouteCode(routeId: Long): RouteRegionCode

    suspend fun getRouteInfos(routeId: Long): List<RouteInfo>

    suspend fun getRouteInfos(routeRegionCode: RouteRegionCode): List<RouteInfo>

    suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop>

    suspend fun getRouteStopShiftEtas(routeId: Long, stopId: Long): List<RouteStopShiftEta>

    suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteRegionCode>
}
