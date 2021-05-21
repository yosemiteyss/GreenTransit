//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.*
import retrofit2.http.GET
import retrofit2.http.Path

interface GMBService {

    @GET("/route/{region}")
    suspend fun getRegionRoutes(@Path("region") region: String): RouteCodesNetworkDto

    @GET("/stop/{stopId}")
    suspend fun getStopInfo(@Path("stopId") stopId: Long): StopInfoDto

    @GET("/stop-route/{stopId}")
    suspend fun getStopRouteList(@Path("stopId") stopId: Long): List<StopRouteDto>

    @GET("/eta/stop/{stopId}")
    suspend fun getStopEtaRouteList(@Path("stopId") stopId: Long): List<StopRouteEtaDto>

    @GET("/route/{routeId}")
    suspend fun getRouteInfo(@Path("routeId") routeId: Long): List<RouteInfoDto>

    @GET("/route/{region}/{routeCode}")
    suspend fun getRouteInfo(
        @Path("region") region: String,
        @Path("routeCode") routeCode: String
    ): List<RouteInfoDto>

    @GET("/route-stop/{routeId}/{routeSeq}")
    suspend fun getRouteStops(
        @Path("routeId") routeId: Long,
        @Path("routeSeq") routeSeq: Int
    ): RouteStopListDto

    @GET("/eta/route-stop/{routeId}/{stopId}")
    suspend fun getRouteStopEtaList(
        @Path("routeId") routeId: Long,
        @Path("stopId") stopId: Long
    ): List<RouteStopEtaDto>
}