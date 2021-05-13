package com.yosemiteyss.greentransit.data.dtos

import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_CHIN_SHORT
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_CHIN_TEXT
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_COUNT_DIST
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_CURRENT_STATUS
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_ENG_SHORT
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_ENG_TEXT
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_MSG_ID
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_DTO_REF_DATE
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "message", strict = false)
data class TrafficNewsDto(
    @field:Element(name = TRAFFIC_NEWS_DTO_MSG_ID)
    var msgID: String? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_CURRENT_STATUS)
    var currentStatus: Int ? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_CHIN_TEXT)
    var chinText: String ? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_CHIN_SHORT)
    var chinShort: String ? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_ENG_TEXT)
    var engText: String ? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_ENG_SHORT)
    var engShort: String ? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_REF_DATE)
    var referenceDate: String? = null,

    @field:Element(name = TRAFFIC_NEWS_DTO_COUNT_DIST)
    var CountofDistrict: Int? = null,
)