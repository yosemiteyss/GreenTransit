package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_NETWORK_DTO_ROUTES

/**
 * Created by kevin on 16/5/2021
 */

data class RouteCodesNetworkDto(
    @SerializedName(ROUTE_CODES_NETWORK_DTO_ROUTES)
    val routeCodes: List<String>,
)