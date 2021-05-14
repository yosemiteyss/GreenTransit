package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.TrafficNewsMessagesDto
import retrofit2.http.GET

/**
 * Created by kevin on 11/5/2021
 */

interface TrafficNewsService {

    @GET("/td/en/specialtrafficnews.xml")
    suspend fun getTrafficNews(): TrafficNewsMessagesDto
}