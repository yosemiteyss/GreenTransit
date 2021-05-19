package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_STOP_ID
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_STOP_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_LIST_DTO_ROUTE_STOPS

/**
 * Created by kevin on 18/5/2021
 */

data class RouteStopListDto(
    @SerializedName(ROUTE_STOP_LIST_DTO_ROUTE_STOPS)
    val routeStops: List<RouteStopDto>
)

data class RouteStopDto(
    @SerializedName(ROUTE_STOP_DTO_STOP_ID)
    val stopId: Long,

    @SerializedName(ROUTE_STOP_DTO_STOP_SEQ)
    val stopSeq: Int,

    @SerializedName(ROUTE_STOP_DTO_NAME_TC)
    val nameTC: String,

    @SerializedName(ROUTE_STOP_DTO_NAME_SC)
    val nameSC: String,

    @SerializedName(ROUTE_STOP_DTO_NAME_EN)
    val nameEN: String,
)