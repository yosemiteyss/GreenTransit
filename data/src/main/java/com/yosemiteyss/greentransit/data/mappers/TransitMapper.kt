package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_HKI
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_KLN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_NT
import com.yosemiteyss.greentransit.data.dtos.RouteInfoDto
import com.yosemiteyss.greentransit.data.dtos.StopEtaRouteDto
import com.yosemiteyss.greentransit.data.dtos.StopLocationDto
import com.yosemiteyss.greentransit.domain.models.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by kevin on 12/5/2021
 */

class TransitMapper @Inject constructor() {

    fun toStopLocation(dto: StopLocationDto): StopLocation {
        return StopLocation(
            stopId = dto.stopId!!,
            location = Coordinate(dto.location!!.latitude, dto.location.longitude)
        )
    }

    fun toRouteInfo(dto: RouteInfoDto): RouteInfo {
        return RouteInfo(
            id = dto.id!!,
            seq = dto.seq!!,
            code = dto.code!!,
            region = toRouteRegion(dto.region!!)
        )
    }

    fun toRouteRegion(region: String): RouteRegion {
        return when (region) {
            ROUTE_REGION_KLN -> RouteRegion.KLN
            ROUTE_REGION_HKI -> RouteRegion.HKI
            ROUTE_REGION_NT -> RouteRegion.NT
            else -> throw Exception("Invalid region: $region")
        }
    }

    fun toStopEtaItems(routeDto: StopEtaRouteDto): List<StopEtaItem> {
        return routeDto.etaItems.map {
            StopEtaItem(
                routeId = routeDto.routeId,
                routeSeq = routeDto.routeSeq,
                etaSeq = it.seq,
                etaMin = it.diff,
                etaTimestamp = parseTimestamp(it.timestamp)
            )
        }
    }

    private fun parseTimestamp(timestamp: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")
        }

        return sdf.parse(timestamp)!!
    }
}