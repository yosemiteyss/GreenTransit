//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.models

import java.util.*

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