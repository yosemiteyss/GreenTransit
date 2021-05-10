package com.yosemiteyss.greentransit.api

import com.yosemiteyss.greentransit.models.RoutesDto
import com.yosemiteyss.greentransit.states.Resource
import retrofit2.http.GET

/**
 * Created by kevin on 11/5/2021
 */

interface GMBService {

    @GET("/route/HKI")
    suspend fun getRoutesHKI(): Resource<RoutesDto>

    @GET("/route/NT")
    suspend fun getRoutesNT(): Resource<RoutesDto>

    @GET("/route/KLN")
    suspend fun getRoutesKLN(): Resource<RoutesDto>
}