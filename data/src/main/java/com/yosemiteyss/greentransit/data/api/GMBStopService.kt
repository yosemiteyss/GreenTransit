package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.StopInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kevin on 17/5/2021
 */

interface GMBStopService {

    @GET("/stop/{stopId}")
    suspend fun getStopInfo(@Path("stopId") stopId: Long): StopInfoDto
}