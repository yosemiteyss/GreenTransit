//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_DESCRIPTION_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_DESCRIPTION_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_DESCRIPTION_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_ETA
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_STOP_ETA_DTO_STOP_SEQ

data class RouteStopEtaDto(
    @SerializedName(ROUTE_STOP_ETA_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(ROUTE_STOP_ETA_DTO_STOP_SEQ)
    val stopSeq: Int,

    @SerializedName(ROUTE_STOP_ETA_DTO_ENABLED)
    val enabled: Boolean,

    @SerializedName(ROUTE_STOP_ETA_DTO_DESCRIPTION_TC)
    val descriptionTC: String?,

    @SerializedName(ROUTE_STOP_ETA_DTO_DESCRIPTION_SC)
    val descriptionSC: String?,

    @SerializedName(ROUTE_STOP_ETA_DTO_DESCRIPTION_EN)
    val descriptionEN: String?,

    @SerializedName(ROUTE_STOP_ETA_DTO_ETA)
    val eta: List<ShiftEtaDto>?
)