package com.yosemiteyss.greentransit.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.google.firebase.firestore.FirebaseFirestore
import com.yosemiteyss.greentransit.data.api.GMBService
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_GEO_HASH
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODES_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODE_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_CODE_DTO_ROUTE_IDS
import com.yosemiteyss.greentransit.data.mappers.TransitMapper
import com.yosemiteyss.greentransit.data.paging.RegionRoutesPagingSource
import com.yosemiteyss.greentransit.data.utils.getAwaitResult
import com.yosemiteyss.greentransit.domain.models.*
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

private const val ARRAY_CONTAINS_MAX = 10

class TransitRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val gmbService: GMBService,
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

    override suspend fun getRegionRoutes(region: RouteRegion): Flow<PagingData<RouteRegionCode>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 15,
                pageSize = 10
            ),
            pagingSourceFactory = {
                RegionRoutesPagingSource(
                    firestore = firestore,
                    region = transitMapper.toRouteRegion(region)
                )
            }
        )
            .flow
            .map { pagingData ->
                pagingData.map { transitMapper.toRouteCode(it) }
            }
    }

    override suspend fun getStopInfo(stopId: Long): StopInfo {
        return gmbService.getStopInfo(stopId)
            .let { transitMapper.toStopInfo(it) }
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

    override suspend fun getRouteCode(routeId: Long): RouteRegionCode {
        return firestore.collection(ROUTE_REGION_CODES_COLLECTION)
            .whereArrayContains(ROUTE_REGION_CODE_DTO_ROUTE_IDS, routeId)
            .limit(1)
            .getAwaitResult(transitMapper::toRouteCode)
            .first()
    }

    override suspend fun getRouteInfos(routeId: Long): List<RouteInfo> {
        return gmbService.getRouteInfo(routeId)
            .map { transitMapper.toRouteInfo(it) }
    }

    override suspend fun getRouteInfos(routeRegionCode: RouteRegionCode): List<RouteInfo> {
        return gmbService.getRouteInfo(
            transitMapper.toRouteRegion(routeRegionCode.region),
            routeRegionCode.code
        ).map { transitMapper.toRouteInfo(it) }
    }

    override suspend fun getRouteStops(routeId: Long, routeSeq: Int): List<RouteStop> {
        return gmbService.getRouteStops(routeId, routeSeq).routeStops
            .map { transitMapper.toRouteStop(it) }
    }

    override suspend fun getRouteStopShiftEtas(routeId: Long, stopId: Long): List<RouteStopShiftEta> {
        return gmbService.getRouteStopEtaList(routeId, stopId)
            .map { transitMapper.toRouteStopShiftEta(it) }
            .flatten()
    }

    @ExperimentalStdlibApi
    override suspend fun searchRoute(query: String, numOfRoutes: Int): List<RouteRegionCode> {
        return firestore.collection(ROUTE_REGION_CODES_COLLECTION)
            .whereGreaterThanOrEqualTo(ROUTE_REGION_CODE_DTO_CODE, query.uppercase())
            .whereLessThanOrEqualTo(ROUTE_REGION_CODE_DTO_CODE, (query + '\uf8ff').uppercase())
            .limit(numOfRoutes.toLong())
            .getAwaitResult(transitMapper::toRouteCode)
    }
}