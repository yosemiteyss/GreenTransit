package com.yosemiteyss.greentransit.domain.models

import java.util.*

/**
 * Represent a route shift's eta info for a specific stop.
 * Combined with route code.
 */

data class StopRouteShiftEtaResult(
    val routeId: Long,
    val routeSeq: Int,
    val routeCode: RouteCode,
    val dest: String,
    val etaMin: Int,
    val etaDate: Date,
    val remarks: String?
)