package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_DEST_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_DEST_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_DEST_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_ORIG_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_ORIG_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_ORIG_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_REMARKS_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_DIRECTION_DTO_ROUTE_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_EN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_SC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DESCRIPTION_TC
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_DIRECTIONS
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_ROUTE_ID

/**
 * Created by kevin on 18/5/2021
 */

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

data class RouteDirectionDto(
    @SerializedName(ROUTE_DIRECTION_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(ROUTE_DIRECTION_DTO_ORIG_TC)
    val originTC: String,

    @SerializedName(ROUTE_DIRECTION_DTO_ORIG_SC)
    val originSC: String,

    @SerializedName(ROUTE_DIRECTION_DTO_ORIG_EN)
    val originEN: String,

    @SerializedName(ROUTE_DIRECTION_DTO_DEST_TC)
    val destTC: String,

    @SerializedName(ROUTE_DIRECTION_DTO_DEST_SC)
    val destSC: String,

    @SerializedName(ROUTE_DIRECTION_DTO_DEST_EN)
    val destEN: String,

    @SerializedName(ROUTE_DIRECTION_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(ROUTE_DIRECTION_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(ROUTE_DIRECTION_DTO_REMARKS_EN)
    val remarksEN: String?
)