package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.RoutesDto
import retrofit2.http.GET

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
}