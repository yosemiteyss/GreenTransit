package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_LOCATION_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_LOCATION_DTO_LOCATION
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_LOCATION_GEO_HASH

/**
 * Created by kevin on 12/5/2021
 */

data class StopLocationDto(
    @JvmField
    @PropertyName(STOP_LOCATION_DTO_ID)
    val stopId: Long? = null,

    @JvmField
    @PropertyName(STOP_LOCATION_DTO_LOCATION)
    val location: GeoPoint? = null,

    @JvmField
    @PropertyName(STOP_LOCATION_GEO_HASH)
    val geohash: String? = null
)