package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.RoutesDto
import com.yosemiteyss.greentransit.domain.states.Resource
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