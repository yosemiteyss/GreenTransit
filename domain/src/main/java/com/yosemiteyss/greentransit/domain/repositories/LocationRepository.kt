package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.Coordinate
import kotlinx.coroutines.flow.Flow

/**
 * Created by kevin on 5/6/2021
 */

interface LocationRepository {

    fun getDeviceLocation(): Flow<Coordinate>

    fun getDeviceOrientation(): Flow<Float>
}