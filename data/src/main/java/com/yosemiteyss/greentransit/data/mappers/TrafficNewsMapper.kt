package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_STATUS_NEW
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_STATUS_UPDATED
import com.yosemiteyss.greentransit.data.dtos.TrafficNewsMessagesDto
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by kevin on 14/5/2021
 */

class TrafficNewsMapper @Inject constructor() {

    fun toTrafficNews(dto: TrafficNewsMessagesDto): List<TrafficNews> {
        return dto.messageList.map {
            TrafficNews(
                msgId = it.msgID!!,
                currentStatus = toTrafficNewsStatus(it.currentStatus),
                engShort = it.engShort!!,
                referenceDate = parseTimestamp(it.referenceDate!!)
            )
        }
    }

    fun toTrafficNewsStatus(currentStatus: Int?) : TrafficNewsStatus {
        return when (currentStatus) {
            TRAFFIC_NEWS_STATUS_NEW -> TrafficNewsStatus.NEW
            TRAFFIC_NEWS_STATUS_UPDATED -> TrafficNewsStatus.UPDATED
            else -> throw Exception("Invalid status: $currentStatus")
        }
    }

    private fun parseTimestamp(timestamp: String): Date {
        val sdf = SimpleDateFormat("yyyy/MM/dd a hh:mm:ss", Locale.CHINA)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")
        return sdf.parse(timestamp) ?: throw Exception("Unable to parse date.")
    }
}