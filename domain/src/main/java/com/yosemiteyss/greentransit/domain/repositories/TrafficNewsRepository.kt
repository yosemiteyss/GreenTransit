package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.TrafficNews

/**
 * Created by kevin on 14/5/2021
 */

interface TrafficNewsRepository {
    // TODO: getTrafficNews()

    suspend fun getTrafficNews(): List<TrafficNews>
}