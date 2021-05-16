package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_HKI
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_KLN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_NT
import com.yosemiteyss.greentransit.data.dtos.NearbyRouteDto
import com.yosemiteyss.greentransit.data.dtos.NearbyStopDto
import com.yosemiteyss.greentransit.data.dtos.RouteCodeDto
import com.yosemiteyss.greentransit.data.dtos.StopEtaRouteDto
import com.yosemiteyss.greentransit.domain.models.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

class TransitMapper @Inject constructor() {

    fun toNearbyStop(dto: NearbyStopDto): NearbyStop {
        return NearbyStop(
            id = dto.id!!,
            routeId = dto.routeId!!,
            location = Coordinate(dto.location!!.latitude, dto.location.longitude)
        )
    }

    fun toNearbyRoute(dto: NearbyRouteDto): NearbyRoute {
        return NearbyRoute(
            id = dto.id!!,
            seq = dto.seq!!,
            code = dto.code!!,
            origin = dto.originEN!!,
            dest = dto.destEN!!,
            region = toRouteRegion(dto.region!!)
        )
    }

    fun toRouteCode(dto: RouteCodeDto): RouteCode {
        return RouteCode(
            code = dto.code!!,
            region = toRouteRegion(dto.region!!)
        )
    }

    fun toStopEtaShift(routeDto: StopEtaRouteDto): List<StopEtaShift> {
        return routeDto.shifts.map {
            StopEtaShift(
                routeId = routeDto.routeId,
                routeSeq = routeDto.routeSeq,
                etaSeq = it.seq,
                etaMin = it.diff,
                etaTimestamp = parseTimestamp(it.timestamp)
            )
        }
    }

    fun toRouteRegion(region: String): RouteRegion {
        return when (region) {
            ROUTE_REGION_KLN -> RouteRegion.KLN
            ROUTE_REGION_HKI -> RouteRegion.HKI
            ROUTE_REGION_NT -> RouteRegion.NT
            else -> throw Exception("Invalid region: $region")
        }
    }

    fun toRouteRegion(region: RouteRegion): String {
        return when (region) {
            RouteRegion.KLN -> ROUTE_REGION_KLN
            RouteRegion.HKI -> ROUTE_REGION_HKI
            RouteRegion.NT -> ROUTE_REGION_NT
        }
    }

    private fun parseTimestamp(timestamp: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")
        return sdf.parse(timestamp)!!
    }
}