package com.yosemiteyss.greentransit.data.dtos

import com.google.gson.annotations.SerializedName
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTES_DTO_ROUTES

/**
 * Created by kevin on 11/5/2021
 */

data class RoutesDto(
    @SerializedName(ROUTES_DTO_ROUTES)
    val routes: List<String>
)