package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants

/**
 * Represent the direction of a specific route.
 */

data class RouteDirectionDto(
    @SerializedName(Constants.ROUTE_DIRECTION_DTO_ROUTE_SEQ)
    val routeSeq: Int,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_ORIG_TC)
    val originTC: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_ORIG_SC)
    val originSC: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_ORIG_EN)
    val originEN: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_DEST_TC)
    val destTC: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_DEST_SC)
    val destSC: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_DEST_EN)
    val destEN: String,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(Constants.ROUTE_DIRECTION_DTO_REMARKS_EN)
    val remarksEN: String?
)