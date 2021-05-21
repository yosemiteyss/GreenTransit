//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ETA
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ROUTE_ID
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ROUTE_ETA_DTO_STOP_SEQ

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