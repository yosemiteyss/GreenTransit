package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_EN
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_SC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_NAME_TC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_ROUTE_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_DTO_STOP_SEQ

/**
 * Created by kevin on 17/5/2021
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