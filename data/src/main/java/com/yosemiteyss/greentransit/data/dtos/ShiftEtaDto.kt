package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_DIFF
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_TC
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_TIMESTAMP

/**
 * Represent a shift's eta info.
 */

data class ShiftEtaDto(
    @SerializedName(SHIFT_ETA_DTO_SEQ)
    val etaSeq: Int,

    @SerializedName(SHIFT_ETA_DTO_DIFF)
    val etaDiff: Int,

    @SerializedName(SHIFT_ETA_DTO_REMARKS_TC)
    val remarksTC: String?,

    @SerializedName(SHIFT_ETA_DTO_REMARKS_SC)
    val remarksSC: String?,

    @SerializedName(SHIFT_ETA_DTO_REMARKS_EN)
    val remarksEN: String?,

    @SerializedName(SHIFT_ETA_DTO_TIMESTAMP)
    val timestamp: String
)