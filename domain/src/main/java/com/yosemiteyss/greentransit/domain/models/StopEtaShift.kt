package com.yosemiteyss.greentransit.domain.models

import java.util.*

/**
 * Created by kevin on 13/5/2021
 */

data class StopEtaShift(
    val routeId: Long,
    val routeSeq: Int,
    val etaSeq: Int,    // Order of the eta entry (the smaller, the sooner)
    val etaMin: Int,
    val etaTimestamp: Date
)