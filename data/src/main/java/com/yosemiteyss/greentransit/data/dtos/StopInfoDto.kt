package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_ENABLED
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_INFO_DTO_REMARKS_TC

/**
 * Created by kevin on 17/5/2021
 */

data class StopInfoDto(
    @SerializedName(STOP_INFO_DTO_ENABLED)
    val enabled: Boolean,

    @SerializedName(STOP_INFO_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(STOP_INFO_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(STOP_INFO_DTO_REMARKS_EN)
    val remarksEN: String?
)