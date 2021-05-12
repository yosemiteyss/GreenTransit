package com.yosemiteyss.greentransit.data.mappers

import com.yosemiteyss.greentransit.data.dtos.StopLocationDto
import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.StopLocation
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
}