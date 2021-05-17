package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 18/5/2021
 */

data class StopEtaShiftWithCode(
    val routeCode: RouteCode,
    val etaShift: StopEtaShift
)