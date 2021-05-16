package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.RoutesDto
import com.yosemiteyss.greentransit.data.dtos.StopEtaRouteDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kevin on 11/5/2021
 */

interface GMBService {

    @GET("/route/HKI")
    suspend fun getRoutesHKI(): RoutesDto

    @GET("/route/NT")
    suspend fun getRoutesNT(): RoutesDto

    @GET("/route/KLN")
    suspend fun getRoutesKLN(): RoutesDto

    @GET("/eta/stop/{stopId}")
    suspend fun getStopEtaRouteList(@Path("stopId") stopId: Long): List<StopEtaRouteDto>
}