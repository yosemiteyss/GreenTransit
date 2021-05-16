package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 12/5/2021
 */

data class NearbyStop(
    val id: Long,
    val routeId: Long,
    val location: Coordinate
)