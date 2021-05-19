package com.yosemiteyss.greentransit.domain.repositories

import androidx.paging.PagingData
import com.yosemiteyss.greentransit.domain.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

/**
 * Created by kevin on 12/5/2021
 */

class FakeTransitRepositoryImpl : TransitRepository {

    private var throwNetworkError = false

    val fakeNearbyStops: List<NearbyStop> = MutableList(10) { createFakeNearbyStop() }

    val fakeNearbyRoute: List<NearbyRoute> = MutableList(10) { createFakeNearbyRoute() }

    val fakeStopInfo: StopInfo = createFakeStopInfo()

    val fakeStopRoutes: List<StopRoute> = MutableList(20) { createFakeStopRoute() }

    val fakeStopRouteShiftEtas: List<StopRouteShiftEta> = MutableList(20) { createFakeStopEtaShift() }

    val fakeRouteRegionCodes: List<RouteRegionCode> = MutableList(20) { createFakeRouteCode() }

    val fakeRouteRegionCode: RouteRegionCode = createFakeRouteCode()

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeNearbyStops + fakeNearbyStops
    }

    override suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeNearbyRoute
    }

    override suspend fun getRegionRoutes(region: RouteRegion): Flow<PagingData<RouteRegionCode>> {
        return flowOf(
            PagingData.from(
                fakeRouteRegionCodes.filter { it.region == region }
            )
        )
    }

    override suspend fun getStopInfo(stopId: Long): StopInfo {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopInfo
    }

    override suspend fun getStopRoutes(stopId: Long): List<StopRoute> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopRoutes
    }

    override suspend fun getStopRouteShiftEtas(stopId: Long): List<StopRouteShiftEta> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopRouteShiftEtas
    }

    override suspend fun getRouteCode(routeId: Long): RouteRegionCode {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteRegionCode
    }

    override suspend fun getRouteInfos(routeId: Long): List<RouteInfo> {
        throw Exception("Network error.")
    }

    override suspend fun getRouteInfos(routeRegionCode: RouteRegionCode): List<RouteInfo> {
        throw Exception("Network error.")
    }

    override suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop> {
        throw Exception("Network error.")
    }

    override suspend fun getRouteStopShiftEtas(
        routeId: Long,
        stopId: Long
    ): List<RouteStopShiftEta> {
        throw Exception("Network error.")
    }

    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteRegionCode> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteRegionCodes
    }

    fun setNetworkError(throwError: Boolean) {
        this.throwNetworkError = throwError
    }

    private fun createFakeNearbyStop(): NearbyStop {
        val random = Random()
        return NearbyStop(
            id = random.nextLong(),
            routeId = random.nextLong(),
            location = Coordinate(
                latitude = random.nextDouble(),
                longitude = random.nextDouble()
            )
        )
    }

    private fun createFakeNearbyRoute(): NearbyRoute {
        val random = Random()
        return NearbyRoute(
            id = random.nextLong(),
            seq = random.nextInt(),
            code = UUID.randomUUID().toString(),
            region = RouteRegion.values().random(),
            origin = UUID.randomUUID().toString(),
            dest = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopInfo(): StopInfo {
        val random = Random()
        return StopInfo(
            location = Coordinate(
                latitude = random.nextDouble(),
                longitude = random.nextDouble()
            ),
            enabled = true,
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopRoute(): StopRoute {
        val random = Random()
        return StopRoute(
            routeId = random.nextLong(),
            routeSeq = random.nextInt(),
            stopSeq = random.nextInt(),
            name = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopEtaShift(): StopRouteShiftEta {
        val random = Random()
        return StopRouteShiftEta(
            routeId = random.nextLong(),
            routeSeq = random.nextInt(),
            etaSeq = random.nextInt(),
            etaMin = random.nextInt(),
            etaDate = Date(),
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeRouteCode(): RouteRegionCode {
        return RouteRegionCode(
            code = UUID.randomUUID().toString(),
            region = RouteRegion.values().random()
        )
    }

    private fun createFakeRouteInfo(): RouteInfo {
        return RouteInfo(
            routeId = Random().nextLong(),
            description = UUID.randomUUID().toString(),
            directions = MutableList(20) { createFakeRouteDirection() }
        )
    }

    private fun createFakeRouteDirection(): RouteDirection {
        return RouteDirection(
            routeSeq = Random().nextInt(),
            origin = UUID.randomUUID().toString(),
            dest = UUID.randomUUID().toString(),
            remarks = UUID.randomUUID().toString()
        )
    }
}