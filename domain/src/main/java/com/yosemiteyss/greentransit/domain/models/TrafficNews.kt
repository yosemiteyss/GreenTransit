package com.yosemiteyss.greentransit.domain.models

import java.util.*

/**
 * Created by kevin on 14/5/2021
 */

data class TrafficNews(
    val msgId: String,
    // TODO: Added required fields
    val currentStatus: TrafficNewsStatus,
    val engShort: String,
    val referenceDate: Date
)