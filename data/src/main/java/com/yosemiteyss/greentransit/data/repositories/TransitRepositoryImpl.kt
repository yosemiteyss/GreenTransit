package com.yosemiteyss.greentransit.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.yosemiteyss.greentransit.data.api.GMBService
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_LOCATION_COLLECTION
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_LOCATION_GEO_HASH
import com.yosemiteyss.greentransit.data.mappers.TransitMapper
import com.yosemiteyss.greentransit.data.utils.getAwaitResult
import com.yosemiteyss.greentransit.domain.models.RouteInfo
import com.yosemiteyss.greentransit.domain.models.StopLocation
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

class TransitRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val gmbService: GMBService,
    private val transitMapper: TransitMapper
) : TransitRepository {

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<StopLocation> {
        return firestore.collectionGroup(STOP_LOCATION_COLLECTION)
            .orderBy(STOP_LOCATION_GEO_HASH)
            .startAt(startHash)
            .endAt(endHash)
            .getAwaitResult(transitMapper::toStopLocation)
    }

    override suspend fun getRouteInfo(routeId: Long): RouteInfo {
        return firestore.collectionGroup(ROUTE_INFO_COLLECTION)
            .whereEqualTo(ROUTE_INFO_DTO_ID, routeId)
            .limit(1)
            .getAwaitResult(transitMapper::toRouteInfo)
            .first()
    }
}