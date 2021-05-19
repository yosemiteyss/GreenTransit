package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_EN
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_SC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_TC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_ROUTE_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_STOP_SEQ

/**
 * Represent a route in a specific stop.
 */

data class StopRouteDto(
    @SerializedName(STOP_ROUTE_DTO_ROUTE_ID)
    val routeId: Long,

    @SerializedName(STOP_ROUTE_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(STOP_ROUTE_DTO_STOP_SEQ)
    val stopSeq: Int,

    @SerializedName(STOP_ROUTE_DTO_NAME_TC)
    val nameTC: String,

    @SerializedName(STOP_ROUTE_DTO_NAME_SC)
    val nameSC: String,

    @SerializedName(STOP_ROUTE_DTO_NAME_EN)
    val nameEN: String
)