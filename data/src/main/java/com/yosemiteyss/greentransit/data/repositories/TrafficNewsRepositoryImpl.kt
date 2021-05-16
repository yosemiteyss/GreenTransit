package com.yosemiteyss.greentransit.data.repositories

import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import javax.inject.Inject

/**
 * Created by kevin on 14/5/2021
 */

class TrafficNewsRepositoryImpl @Inject constructor(

) : TrafficNewsRepository {

    override suspend fun getTrafficNews(): List<TrafficNews> {
        return emptyList()
    }
}