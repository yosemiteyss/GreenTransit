package com.yosemiteyss.greentransit.domain.models

/**
 * Created by kevin on 17/5/2021
 */

data class StopRouteWithCode(
    val routeCode: RouteCode,
    val stopRoute: StopRoute
)