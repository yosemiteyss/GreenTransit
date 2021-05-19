package com.yosemiteyss.greentransit.domain.models

import java.util.*

/**
 * Created by kevin on 18/5/2021
 */

/**
 * Represent the latest eta shift of a route stop.
 */
data class RouteStopShiftEta(
    val routeSeq: Int,
    val stopSeq: Int,
    val etaDescription: String?,       // Used with eta is disabled
    val etaEnabled: Boolean,
    val etaSeq: Int?,
    val etaMin: Int?,
    val etaDate: Date?,
    val etaRemarks: String?
)