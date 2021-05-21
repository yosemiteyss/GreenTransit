//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_NAME_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_STOP_ID
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_DTO_STOP_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_LIST_DTO_ROUTE_STOPS

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