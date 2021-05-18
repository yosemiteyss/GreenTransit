package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_HKI
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_KLN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_NT
import com.yosemiteyss.greentransit.data.dtos.*
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

    fun toStopInfo(dto: StopInfoDto): StopInfo {
        return StopInfo(
            location = toCoordinates(dto.coordinates.wgs84),
            enabled = dto.enabled,
            remarks = dto.remarksEN
        )
    }

    fun toStopRoute(dto: StopRouteDto): StopRoute {
        return StopRoute(
            routeId = dto.routeId,
            routeSeq = dto.routeSeq,
            stopSeq = dto.stopSeq,
            name = dto.nameEN
        )
    }

    fun toStopEtaShift(routeDto: StopEtaRouteDto): List<StopEtaShift> {
        return routeDto.shifts?.map { shiftDto ->
            StopEtaShift(
                routeId = routeDto.routeId,
                routeSeq = routeDto.routeSeq,
                etaSeq = shiftDto.seq,
                etaMin = shiftDto.diff,
                etaDate = parseTimestamp(shiftDto.timestamp),
                remarks = shiftDto.remarksEN
            )
        } ?: emptyList()
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

    fun toRouteInfo(dto: RouteInfoDto): RouteInfo {
        return RouteInfo(
            routeId = dto.routeId,
            description = dto.descriptionEN,
            direction = dto.directions.map { toRouteDirection(it) }
        )
    }

    fun toRouteDirection(dto: RouteDirectionDto): RouteDirection {
        return RouteDirection(
            routeSeq = dto.routeSeq,
            origin = dto.originEN,
            dest = dto.destEN,
            remarks = dto.remarksEN
        )
    }

    fun toCoordinates(dto: StopCoordinatesDto): Coordinate {
        return Coordinate(
            latitude = dto.latitude,
            longitude = dto.longitude
        )
    }

    private fun parseTimestamp(timestamp: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")
        return sdf.parse(timestamp) ?: throw Exception("Unable to parse date.")
    }
}