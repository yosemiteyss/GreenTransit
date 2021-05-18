package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 18/5/2021
 */

data class RouteInfo(
    val routeId: Long,
    val description: String,
    val direction: List<RouteDirection>
)

data class RouteDirection(
    val routeSeq: Int,
    val origin: String,
    val dest: String,
    val remarks: String?
)