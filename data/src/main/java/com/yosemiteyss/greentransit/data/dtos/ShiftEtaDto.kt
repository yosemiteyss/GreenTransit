//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_DIFF
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_EN
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_SC
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_REMARKS_TC
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.SHIFT_ETA_DTO_TIMESTAMP

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