//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import com.google.firebase.firestore.PropertyName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_SEARCH_DTO_ROUTE_IDS

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