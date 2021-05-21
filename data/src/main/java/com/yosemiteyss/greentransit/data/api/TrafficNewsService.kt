//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.TrafficNewsMessagesDto
import retrofit2.http.GET

interface TrafficNewsService {

    @GET("/td/en/specialtrafficnews.xml")
    suspend fun getTrafficNews(): TrafficNewsMessagesDto
}