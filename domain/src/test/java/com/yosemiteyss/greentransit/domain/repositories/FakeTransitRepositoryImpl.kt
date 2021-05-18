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

    val fakeStopEtaShifts: List<StopEtaShift> = MutableList(20) { createFakeStopEtaShift() }

    val fakeRegionRoutes: List<RouteCode> = MutableList(20) { createFakeRouteCode() }

    val fakeRouteCode: RouteCode = createFakeRouteCode()

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeNearbyStops + fakeNearbyStops
    }

    override suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeNearbyRoute
    }

    override suspend fun getRegionRoutes(region: RouteRegion): Flow<PagingData<RouteCode>> {
        return flowOf(
            PagingData.from(
                fakeRegionRoutes.filter { it.region == region }
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

    override suspend fun getStopEtaShifts(stopId: Long): List<StopEtaShift> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopEtaShifts
    }

    override suspend fun getRouteCode(routeId: Long): RouteCode {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteCode
    }

    override suspend fun getRouteInfo(routeId: Long): List<RouteInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRegionRoutes
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

    private fun createFakeStopEtaShift(): StopEtaShift {
        val random = Random()
        return StopEtaShift(
            routeId = random.nextLong(),
            routeSeq = random.nextInt(),
            etaSeq = random.nextInt(),
            etaMin = random.nextInt(),
            etaDate = Date(),
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeRouteCode(): RouteCode {
        return RouteCode(
            code = UUID.randomUUID().toString(),
            region = RouteRegion.values().random()
        )
    }

    private fun createFakeRouteInfo(): RouteInfo {
        return RouteInfo(
            routeId = Random().nextLong(),
            description = UUID.randomUUID().toString(),
            direction = MutableList(20) { createFakeRouteDirection() }
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