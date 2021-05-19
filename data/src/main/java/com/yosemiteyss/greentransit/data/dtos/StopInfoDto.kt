package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_COORDINATES_DTO_LATITUDE
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_COORDINATES_DTO_LONGITUDE
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_COORDINATES_LIST_DTO_HK80
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_COORDINATES_LIST_DTO_WGS84
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_COORDINATES
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_TC

/**
 * Represent the details of a specific stop.
 */

data class StopInfoDto(
    @SerializedName(STOP_INFO_DTO_COORDINATES)
    val coordinates: StopCoordinatesListDto,

    @SerializedName(STOP_INFO_DTO_ENABLED)
    val enabled: Boolean,

    @SerializedName(STOP_INFO_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(STOP_INFO_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(STOP_INFO_DTO_REMARKS_EN)
    val remarksEN: String?
)

data class StopCoordinatesListDto(
    @SerializedName(STOP_COORDINATES_LIST_DTO_WGS84)
    val wgs84: StopCoordinatesDto,

    @SerializedName(STOP_COORDINATES_LIST_DTO_HK80)
    val hk80: StopCoordinatesDto
)

data class StopCoordinatesDto(
    @SerializedName(STOP_COORDINATES_DTO_LATITUDE)
    val latitude: Double,

    @SerializedName(STOP_COORDINATES_DTO_LONGITUDE)
    val longitude: Double
)