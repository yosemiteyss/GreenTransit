package com.yosemiteyss.greentransit.data.repositories

import com.yosemiteyss.greentransit.data.api.TrafficNewsService
import com.yosemiteyss.greentransit.data.mappers.TrafficNewsMapper
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import javax.inject.Inject

/**
 * Created by kevin on 14/5/2021
 */

class TrafficNewsRepositoryImpl @Inject constructor(
    private val trafficNewsService: TrafficNewsService,
    private val trafficNewsMapper: TrafficNewsMapper
) : TrafficNewsRepository {

    override suspend fun getTrafficNews(): List<TrafficNews> {
        return trafficNewsMapper.toTrafficNews(trafficNewsService.getTrafficNews())
    }
}