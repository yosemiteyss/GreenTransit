package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_DIFF
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_REMARKS_TC
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.STOP_ETA_SHIFT_DTO_TIMESTAMP

/**
 * Created by kevin on 13/5/2021
 */

data class StopEtaShiftDto(
    @SerializedName(STOP_ETA_SHIFT_DTO_SEQ)
    val seq: Int,

    @SerializedName(STOP_ETA_SHIFT_DTO_DIFF)
    val diff: Int,

    @SerializedName(STOP_ETA_SHIFT_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(STOP_ETA_SHIFT_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(STOP_ETA_SHIFT_DTO_REMARKS_EN)
    val remarksEN: String?,

    @SerializedName(STOP_ETA_SHIFT_DTO_TIMESTAMP)
    val timestamp: String
)