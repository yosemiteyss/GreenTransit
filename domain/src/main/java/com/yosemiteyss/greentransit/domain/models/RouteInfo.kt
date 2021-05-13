package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 13/5/2021
 */

data class RouteInfo(
    val id: Long,
    val seq: Int,
    val code: String,
    val region: RouteRegion
)