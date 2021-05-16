package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 13/5/2021
 */

data class NearbyRoute(
    val id: Long,
    val seq: Int,
    val code: String,
    val origin: String,
    val dest: String,
    val region: RouteRegion,
)