package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 17/5/2021
 */

data class StopInfo(
    val stopId: Long,
    val location: Coordinate,
    val enabled: Boolean,
    val remarks: String?
)