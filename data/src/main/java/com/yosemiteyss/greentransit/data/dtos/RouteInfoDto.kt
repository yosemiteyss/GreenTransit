//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DIRECTIONS
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_ROUTE_ID

data class RouteInfoDto(
    @SerializedName(ROUTE_INFO_DTO_ROUTE_ID)
    val routeId: Long,

    @SerializedName(ROUTE_INFO_DTO_DESCRIPTION_TC)
    val descriptionTC: String,

    @SerializedName(ROUTE_INFO_DTO_DESCRIPTION_SC)
    val descriptionSC: String,

    @SerializedName(ROUTE_INFO_DTO_DESCRIPTION_EN)
    val descriptionEN: String,

    @SerializedName(ROUTE_INFO_DTO_DIRECTIONS)
    val directions: List<RouteDirectionDto>
)

