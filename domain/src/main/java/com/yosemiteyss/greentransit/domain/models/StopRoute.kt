package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 17/5/2021
 */

data class StopRoute(
    val routeId: Long,
    val routeSeq: Int,
    val stopSeq: Int,
    val name: String
)