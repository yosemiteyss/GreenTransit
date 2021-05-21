//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.testshared.repositories

import com.yosemiteyss.greentransit.domain.models.*
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.random.Random

/**
 * Created by kevin on 12/5/2021
 */

class FakeTransitRepositoryImpl : TransitRepository {

    private var throwNetworkError = false

    val fakeNearbyStops = MutableList(10) { createFakeNearbyStop() }.let { it + it }

    val fakeRouteInfoByRegionCode = MutableList(10) { createFakeRouteInfo() }

    val fakeRouteInfoByRouteId = MutableList(10) { createFakeRouteInfo() }

    val fakeRouteStops = MutableList(20) { createFakeRouteStop() }

    val fakeRouteStopShiftEtas = MutableList(20) { createFakeRouteStopShiftEta() }

    val fakeStopRoutes = MutableList(10) { createFakeStopRoute() }

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeNearbyStops
    }

    override suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute> {
        if (throwNetworkError) throw Exception("Network error.")
        return MutableList(10) { createFakeNearbyRoute() }
    }

    override fun getRegionRoutes(region: Region): Flow<Resource<List<RouteCode>>> = flow {
        emit(Resource.Loading())
        if (throwNetworkError) {
            emit(Resource.Error("Network error."))
        } else {
            emit(Resource.Success(MutableList(10) { createFakeRouteCode() }))
        }
    }

    override suspend fun getStopInfo(stopId: Long): StopInfo {
        if (throwNetworkError) throw Exception("Network error.")
        return createFakeStopInfo()
    }

    override suspend fun getStopRoutes(stopId: Long): List<StopRoute> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeStopRoutes
    }

    override suspend fun getStopRouteShiftEtas(stopId: Long): List<StopRouteShiftEta> {
        if (throwNetworkError) throw Exception("Network error.")
        return MutableList(20) { createFakeStopEtaShift() }
    }

    override suspend fun getRouteCode(routeId: Long): RouteCode {
        if (throwNetworkError) throw Exception("Network error.")
        return createFakeRouteCode()
    }

    override suspend fun getRouteInfos(routeId: Long): List<RouteInfo> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteInfoByRouteId
    }

    override suspend fun getRouteInfos(routeCode: RouteCode): List<RouteInfo> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteInfoByRegionCode
    }

    override suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteStops
    }

    override suspend fun getRouteStopShiftEtas(
        routeId: Long,
        stopId: Long
    ): List<RouteStopShiftEta> {
        if (throwNetworkError) throw Exception("Network error.")
        return fakeRouteStopShiftEtas
    }

    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode> {
        if (throwNetworkError) throw Exception("Network error.")
        return MutableList(20) { createFakeRouteCode() }
    }

    fun setNetworkError(throwError: Boolean) {
        this.throwNetworkError = throwError
    }

    private fun createFakeNearbyStop(): NearbyStop {
        return NearbyStop(
            id = Random.nextLong(),
            routeId = Random.nextLong(),
            location = Coordinate(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble()
            )
        )
    }

    private fun createFakeNearbyRoute(): NearbyRoute {
        return NearbyRoute(
            id = Random.nextLong(),
            seq = Random.nextInt(),
            code = UUID.randomUUID().toString(),
            region = Region.values().random(),
            origin = UUID.randomUUID().toString(),
            dest = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopInfo(): StopInfo {
        return StopInfo(
            stopId = Random.nextLong(),
            location = Coordinate(
                latitude = Random.nextDouble(),
                longitude = Random.nextDouble()
            ),
            enabled = true,
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopRoute(): StopRoute {
        return StopRoute(
            routeId = Random.nextLong(),
            routeSeq = Random.nextInt(),
            stopSeq = Random.nextInt(),
            name = UUID.randomUUID().toString()
        )
    }

    private fun createFakeStopEtaShift(): StopRouteShiftEta {
        return StopRouteShiftEta(
            routeId = Random.nextLong(),
            routeSeq = Random.nextInt(),
            etaSeq = Random.nextInt(),
            etaMin = Random.nextInt(),
            etaDate = Date(),
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeRouteCode(): RouteCode {
        return RouteCode(
            code = UUID.randomUUID().toString(),
            region = Region.values().random()
        )
    }

    private fun createFakeRouteInfo(): RouteInfo {
        return RouteInfo(
            routeId = Random.nextLong(),
            description = UUID.randomUUID().toString(),
            directions = MutableList(20) { createFakeRouteDirection() }
        )
    }

    private fun createFakeRouteDirection(): RouteDirection {
        return RouteDirection(
            routeSeq = Random.nextInt(),
            origin = UUID.randomUUID().toString(),
            dest = UUID.randomUUID().toString(),
            remarks = UUID.randomUUID().toString()
        )
    }

    private fun createFakeRouteStop(): RouteStop {
        return RouteStop(
            stopId = Random.nextLong(),
            stopSeq = Random.nextInt(),
            stopName = UUID.randomUUID().toString()
        )
    }

    private fun createFakeRouteStopShiftEta(): RouteStopShiftEta {
        return RouteStopShiftEta(
            routeSeq = Random.nextInt(),
            stopSeq = Random.nextInt(),
            etaDescription = UUID.randomUUID().toString(),
            etaEnabled = Random.nextBoolean(),
            etaSeq = Random.nextInt(),
            etaMin = Random.nextInt(),
            etaDate = Date(Random.nextLong()),
            etaRemarks = UUID.randomUUID().toString()
        )
    }
}