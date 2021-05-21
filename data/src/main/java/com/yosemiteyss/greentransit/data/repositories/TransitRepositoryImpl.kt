//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.yosemiteyss.greentransit.data.api.GMBService
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_GEO_HASH
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_ROUTE_IDS
import com.yosemiteyss.greentransit.data.db.AppDatabase
import com.yosemiteyss.greentransit.data.mappers.TransitMapper
import com.yosemiteyss.greentransit.data.utils.getAwaitResult
import com.yosemiteyss.greentransit.domain.models.*
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import com.yosemiteyss.greentransit.domain.states.Resource
import com.yosemiteyss.greentransit.domain.utils.networkCacheResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val ARRAY_CONTAINS_MAX = 10

class TransitRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val gmbService: GMBService,
    private val appDatabase: AppDatabase,
    private val transitMapper: TransitMapper
) : TransitRepository {

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<NearbyStop> {
        return firestore.collectionGroup(NEARBY_STOP_COLLECTION)
            .orderBy(NEARBY_STOP_DTO_GEO_HASH)
            .startAt(startHash)
            .endAt(endHash)
            .getAwaitResult(transitMapper::toNearbyStop)
    }

    override suspend fun getNearbyRoutes(routeIds: List<Long>): List<NearbyRoute> {
        return firestore.collectionGroup(NEARBY_ROUTE_COLLECTION)
            .whereIn(NEARBY_ROUTE_DTO_ID, routeIds.take(ARRAY_CONTAINS_MAX))
            .getAwaitResult(transitMapper::toNearbyRoute)
    }

    override fun getRegionRoutes(region: Region): Flow<Resource<List<RouteCode>>> {
        return networkCacheResource(
            cacheSource = {
                appDatabase.regionRoutesDao().getRegionRoutes(transitMapper.toRouteRegion(region))
                    .map { transitMapper.fromLocalToRouteCode(it) }
            },
            networkSource = {
                gmbService.getRegionRoutes(transitMapper.toRouteRegion(region))
                    .let { transitMapper.fromNetworkToRouteCode(region, it) }
            },
            updateCache = { routeCodes ->
                appDatabase.regionRoutesDao().insertRoute(routeCodes.map {
                    transitMapper.toRouteCodeLocalDto(it)
                })
            }
        )
    }

    override suspend fun getStopInfo(stopId: Long): StopInfo {
        return gmbService.getStopInfo(stopId)
            .let { transitMapper.toStopInfo(stopId, it) }
    }

    override suspend fun getStopRoutes(stopId: Long): List<StopRoute> {
        return gmbService.getStopRouteList(stopId)
            .map { transitMapper.toStopRoute(it) }
    }

    override suspend fun getStopRouteShiftEtas(stopId: Long): List<StopRouteShiftEta> {
        return gmbService.getStopEtaRouteList(stopId)
            .map { transitMapper.toStopEtaShift(it) }
            .flatten()
    }

    override suspend fun getRouteCode(routeId: Long): RouteCode {
        return firestore.collection(ROUTE_SEARCH_COLLECTION)
            .whereArrayContains(ROUTE_SEARCH_DTO_ROUTE_IDS, routeId)
            .limit(1)
            .getAwaitResult(transitMapper::fromSearchToRouteCode)
            .first()
    }

    override suspend fun getRouteInfos(routeId: Long): List<RouteInfo> {
        return gmbService.getRouteInfo(routeId)
            .map { transitMapper.toRouteInfo(it) }
    }

    override suspend fun getRouteInfos(routeCode: RouteCode): List<RouteInfo> {
        return gmbService.getRouteInfo(
            transitMapper.toRouteRegion(routeCode.region),
            routeCode.code
        ).map { transitMapper.toRouteInfo(it) }
    }

    override suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop> {
        return gmbService.getRouteStops(routeId, routeSeq)
            .let { transitMapper.toRouteStop(it) }
    }

    override suspend fun getRouteStopShiftEtas(routeId: Long, stopId: Long): List<RouteStopShiftEta> {
        return gmbService.getRouteStopEtaList(routeId, stopId)
            .map { transitMapper.toRouteStopShiftEta(it) }
            .flatten()
    }

    @ExperimentalStdlibApi
    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteCode> {
        return firestore.collection(ROUTE_SEARCH_COLLECTION)
            .whereGreaterThanOrEqualTo(ROUTE_SEARCH_DTO_CODE, query.uppercase())
            .whereLessThanOrEqualTo(ROUTE_SEARCH_DTO_CODE, (query + '\uf8ff').uppercase())
            .limit(numOfRoutes.toLong())
            .getAwaitResult(transitMapper::fromSearchToRouteCode)
    }
}