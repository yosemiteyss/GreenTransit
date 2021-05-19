package com.yosemiteyss.greentransit.domain.models

/**
 * Represent a route's info for a specific stop.
 * Combined with route code.
 */

data class StopRouteResult(
    val routeRegionCode: RouteRegionCode,
    val stopRoute: StopRoute
)