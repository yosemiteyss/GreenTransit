package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ETA
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ROUTE_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_STOP_SEQ

/**
 * Represent a list of shift eta of a specific route for a specific stop.
 */

data class StopRouteEtaDto(
    @SerializedName(STOP_ROUTE_ETA_DTO_ROUTE_ID)
    val routeId: Long,

    @SerializedName(STOP_ROUTE_ETA_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(STOP_ROUTE_ETA_DTO_STOP_SEQ)
    val stopSeq: Int,

    @SerializedName(STOP_ROUTE_ETA_DTO_ENABLED)
    val enabled: Boolean,

    @SerializedName(STOP_ROUTE_ETA_DTO_ETA)
    val etaShifts: List<ShiftEtaDto>?
)