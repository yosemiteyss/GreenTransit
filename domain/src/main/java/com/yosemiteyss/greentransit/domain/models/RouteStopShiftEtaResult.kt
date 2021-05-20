package com.yosemiteyss.greentransit.domain.models

/**
 * Represent a stop's latest eta info for a specific route.
 * Combined with route stop.
 */

data class RouteStopShiftEtaResult(
    val routeStop: RouteStop,
    val routeStopShiftEta: RouteStopShiftEta? = null
)