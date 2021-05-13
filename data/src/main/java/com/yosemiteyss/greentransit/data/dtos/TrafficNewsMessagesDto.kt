package com.yosemiteyss.greentransit.data.dtos

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "body", strict = false)
data class TrafficNewsMessagesDto @JvmOverloads constructor (
    @field:ElementList(inline = true)
    var messageList: ArrayList<TrafficNewsDto> = arrayListOf<TrafficNewsDto>()
)
