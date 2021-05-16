package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_ROUTE_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_ROUTE_DTO_ETA
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_ROUTE_DTO_ROUTE_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_ROUTE_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_ROUTE_DTO_STOP_SEQ

/**
 * Created by kevin on 13/5/2021
 */

data class StopEtaRouteDto(
    @SerializedName(STOP_ETA_ROUTE_DTO_ROUTE_ID)
    val routeId: Long,

    @SerializedName(STOP_ETA_ROUTE_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(STOP_ETA_ROUTE_DTO_STOP_SEQ)
    val stopSeq: Int,

    @SerializedName(STOP_ETA_ROUTE_DTO_ENABLED)
    val enabled: Boolean,

    @SerializedName(STOP_ETA_ROUTE_DTO_ETA)
    val shifts: List<StopEtaShiftDto>
)