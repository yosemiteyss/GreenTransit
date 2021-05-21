//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.repositories

import com.yosemiteyss.greentransit.data.api.TrafficNewsService
import com.yosemiteyss.greentransit.data.mappers.TrafficNewsMapper
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import javax.inject.Inject

class TrafficNewsRepositoryImpl @Inject constructor(
    private val trafficNewsService: TrafficNewsService,
    private val trafficNewsMapper: TrafficNewsMapper
) : TrafficNewsRepository {

    override suspend fun getTrafficNews(): List<TrafficNews> {
        return trafficNewsMapper.toTrafficNews(trafficNewsService.getTrafficNews())
    }
}