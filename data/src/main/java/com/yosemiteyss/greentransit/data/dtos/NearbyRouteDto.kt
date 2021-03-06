//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_DEST_EN
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_DEST_SC
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_DEST_TC
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ORIGIN_EN
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ORIGIN_SC
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_ORIGIN_TC
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_SEQ
import com.yosemiteyss.greentransit.data.constants.Constants.NEARBY_ROUTE_DTO_STOP_IDS

data class NearbyRouteDto(
    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_ID)
    val id: Long? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_SEQ)
    val seq: Int? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_CODE)
    val code: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_ORIGIN_TC)
    val originTC: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_ORIGIN_SC)
    val originSC: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_ORIGIN_EN)
    val originEN: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_DEST_TC)
    val destTC: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_DEST_SC)
    val destSC: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_DEST_EN)
    val destEN: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_REGION)
    val region: String? = null,

    @JvmField
    @PropertyName(NEARBY_ROUTE_DTO_STOP_IDS)
    val stopIds: List<Long>? = null
)