package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODE_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODE_DTO_REGION

/**
 * Created by kevin on 16/5/2021
 */

data class RouteCodeDto(
    @JvmField
    @PropertyName(ROUTE_CODE_DTO_CODE)
    val code: String? = null,

    @JvmField
    @PropertyName(ROUTE_CODE_DTO_REGION)
    val region: String? = null
)