package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_GEO_HASH
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_LOCATION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_ROUTE_ID

/**
 * Created by kevin on 12/5/2021
 */

data class NearbyStopDto(
    @JvmField
    @PropertyName(NEARBY_STOP_DTO_ID)
    val id: Long? = null,

    @JvmField
    @PropertyName(NEARBY_STOP_DTO_ROUTE_ID)
    val routeId: Long? = null,

    @JvmField
    @PropertyName(NEARBY_STOP_DTO_LOCATION)
    val location: GeoPoint? = null,

    @JvmField
    @PropertyName(NEARBY_STOP_DTO_GEO_HASH)
    val geohash: String? = null
)