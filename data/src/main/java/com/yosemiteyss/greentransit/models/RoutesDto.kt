package com.yosemiteyss.greentransit.models

import com.google.gson.annotations.SerializedName

/**
 * Created by kevin on 11/5/2021
 */

data class RoutesDto(
    @SerializedName("routes")
    val routes: List<String>
)