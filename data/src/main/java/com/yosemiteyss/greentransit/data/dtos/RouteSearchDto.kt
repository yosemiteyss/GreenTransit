package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_ROUTE_IDS

/**
 * Created by kevin on 20/5/2021
 */

data class RouteSearchDto(
    @JvmField
    @PropertyName(ROUTE_SEARCH_DTO_CODE)
    val code: String? = null,

    @JvmField
    @PropertyName(ROUTE_SEARCH_DTO_REGION)
    val region: String? = null,

    @JvmField
    @PropertyName(ROUTE_SEARCH_DTO_ROUTE_IDS)
    val routeIds: List<Long>? = null,
)