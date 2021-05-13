package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_ID
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_INFO_DTO_SEQ

/**
 * Created by kevin on 13/5/2021
 */

data class RouteInfoDto(
    @JvmField
    @PropertyName(ROUTE_INFO_DTO_ID)
    val id: Long? = null,

    @JvmField
    @PropertyName(ROUTE_INFO_DTO_SEQ)
    val seq: Int? = null,

    @JvmField
    @PropertyName(ROUTE_INFO_DTO_CODE)
    val code: String? = null,

    @JvmField
    @PropertyName(ROUTE_INFO_DTO_REGION)
    val region: String? = null
)