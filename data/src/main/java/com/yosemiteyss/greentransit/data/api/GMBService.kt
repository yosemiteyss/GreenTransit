package com.yosemiteyss.greentransit.data.api

import com.yosemiteyss.greentransit.data.dtos.*
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kevin on 11/5/2021
 */

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