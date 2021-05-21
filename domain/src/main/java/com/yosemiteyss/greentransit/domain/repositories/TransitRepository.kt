//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.*
import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.flow.Flow

interface TransitRepository {

    suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop>

    suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute>

    fun getRegionRoutes(region: Region): Flow<Resource<List<RouteCode>>>

    suspend fun getStopInfo(stopId: Long): StopInfo

    suspend fun getStopRoutes(stopId: Long): List<StopRoute>

    suspend fun getStopRouteShiftEtas(stopId: Long): List<StopRouteShiftEta>

    suspend fun getRouteCode(routeId: Long): RouteCode

    suspend fun getRouteInfos(routeId: Long): List<RouteInfo>

    suspend fun getRouteInfos(routeCode: RouteCode): List<RouteInfo>

    suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop>

    suspend fun getRouteStopShiftEtas(routeId: Long, stopId: Long): List<RouteStopShiftEta>

    suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode>
}
