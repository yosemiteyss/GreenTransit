package com.yosemiteyss.greentransit.domain.models

import java.util.*

/**
 * Created by kevin on 14/5/2021
 */

data class TrafficNews(
    val msgId: String,
    val currentStatus: TrafficNewsStatus,
    val engShort: String,
    val referenceDate: Date
)

enum class TrafficNewsStatus(val status: Int) {
    NEW(2),
    UPDATED(3)
}