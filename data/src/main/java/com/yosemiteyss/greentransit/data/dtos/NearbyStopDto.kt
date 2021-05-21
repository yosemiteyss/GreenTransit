//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_GEO_HASH
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_LOCATION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_STOP_DTO_ROUTE_ID

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