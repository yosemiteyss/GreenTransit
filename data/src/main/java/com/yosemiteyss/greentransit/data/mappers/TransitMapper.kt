//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_HKI
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_KLN
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_REGION_NT
import com.yosemiteyss.greentransit.data.dtos.*
import com.yosemiteyss.greentransit.domain.models.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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

    fun fromSearchToRouteCode(dto: RouteSearchDto): RouteCode {
        return RouteCode(
            code = dto.code!!,
            region = toRouteRegion(dto.region!!)
        )
    }

    fun toRouteCodes(region: Region, dto: RouteCodesDto): List<RouteCode> {
        return dto.routeCodes.map {
            RouteCode(code = it, region = region)
        }
    }

    fun toStopInfo(stopId: Long, dto: StopInfoDto): StopInfo {
        return StopInfo(
            stopId = stopId,
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

    fun toStopEtaShift(routeEtaDto: StopRouteEtaDto): List<StopRouteShiftEta> {
        return routeEtaDto.etaShifts?.map { shiftDto ->
            StopRouteShiftEta(
                routeId = routeEtaDto.routeId,
                routeSeq = routeEtaDto.routeSeq,
                etaSeq = shiftDto.etaSeq,
                etaMin = shiftDto.etaDiff,
                etaDate = parseTimestamp(shiftDto.timestamp),
                remarks = shiftDto.remarksEN
            )
        } ?: emptyList()
    }

    fun toRouteRegion(region: String): Region {
        return when (region) {
            ROUTE_REGION_KLN -> Region.KLN
            ROUTE_REGION_HKI -> Region.HKI
            ROUTE_REGION_NT -> Region.NT
            else -> throw Exception("Invalid region: $region")
        }
    }

    fun toRouteRegion(region: Region): String {
        return when (region) {
            Region.KLN -> ROUTE_REGION_KLN
            Region.HKI -> ROUTE_REGION_HKI
            Region.NT -> ROUTE_REGION_NT
        }
    }

    fun toRouteInfo(dto: RouteInfoDto): RouteInfo {
        return RouteInfo(
            routeId = dto.routeId,
            description = dto.descriptionEN,
            directions = dto.directions.map { toRouteDirection(it) }
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

    fun toRouteStop(dto: RouteStopListDto): List<RouteStop> {
        return dto.routeStops.map {
            RouteStop(
                stopId = it.stopId,
                stopSeq = it.stopSeq,
                stopName = it.nameEN
            )
        }
    }

    fun toRouteStopShiftEta(dto: RouteStopEtaDto): List<RouteStopShiftEta> {
        return dto.eta?.map {
            RouteStopShiftEta(
                routeSeq = dto.routeSeq,
                stopSeq = dto.stopSeq,
                etaDescription = dto.descriptionEN,
                etaEnabled = dto.enabled,
                etaSeq = it.etaSeq,
                etaMin = it.etaDiff,
                etaDate = parseTimestamp(it.timestamp),
                etaRemarks = it.remarksEN
            )
        } ?: emptyList()
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