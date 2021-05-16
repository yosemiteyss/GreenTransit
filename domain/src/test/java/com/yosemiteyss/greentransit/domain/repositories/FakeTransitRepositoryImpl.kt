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

    val fakeNearbyStops: List<NearbyStop> = MutableList(5) {
        createFakeNearbyStop()
    }

    val fakeNearbyRoute: List<NearbyRoute> = MutableList(5) {
        createFakeNearbyRoute()
    }

    val fakeStopEtaShiftList: List<StopEtaShift> = MutableList(10) {
        createFakeStopEtaShift()
    }

    val fakeRouteCodes: List<RouteCode> = MutableList(20) {
        createFakeRouteCode()
    }

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
                fakeRouteCodes.filter { it.region == region }
            )
        )
    }

    override suspend fun getStopEtaShiftList(stopId: Long): List<StopEtaShift> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopEtaShiftList
    }

    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteCodes
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

    private fun createFakeStopEtaShift(): StopEtaShift {
        val random = Random()
        return StopEtaShift(
            routeId = random.nextLong(),
            routeSeq = random.nextInt(),
            etaSeq = random.nextInt(),
            etaMin = random.nextInt(),
            etaTimestamp = Date()
        )
    }

    private fun createFakeRouteCode(): RouteCode {
        return RouteCode(
            code = UUID.randomUUID().toString(),
            region = RouteRegion.values().random()
        )
    }
}