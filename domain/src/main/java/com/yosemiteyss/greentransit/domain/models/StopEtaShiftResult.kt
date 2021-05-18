package com.yosemiteyss.greentransit.domain.models

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kevin on 18/5/2021
 */

data class StopEtaResult(
    val routeId: Long,
    val routeSeq: Int,
    val routeCode: RouteCode,
    val dest: String,
    val etaMin: Int,
    val etaDate: Date,
    val remarks: String?
)

fun StopEtaResult.getEtaTimeString(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")
    return sdf.format(etaDate)
}