//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.models

import java.util.*

data class StopRouteShiftEta(
    val routeId: Long,
    val routeSeq: Int,
    val etaSeq: Int,    // Order of the eta entry (the smaller the number, the sooner the shift)
    val etaMin: Int,
    val etaDate: Date,
    val remarks: String?
)