//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.testshared.repositories

import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import java.util.*

class FakeTrafficNewsRepositoryImpl : TrafficNewsRepository {

    private var throwNetworkError = false
    private var dontMakeData = false

    val fakeTrafficNews: List<TrafficNews> = MutableList(5) { createFakeTrafficNews() }

    override suspend fun getTrafficNews(): List<TrafficNews> {
        if (throwNetworkError) throw Exception("Network error.")
        return if (dontMakeData) {
            emptyList()
        } else {
            fakeTrafficNews
        }
    }

    fun setDontMakeData(dontMakeData: Boolean) {
        this.dontMakeData = dontMakeData
    }

    fun setNetworkError(throwError: Boolean) {
        this.throwNetworkError = throwError
    }

    private fun createFakeTrafficNews(): TrafficNews {
        val randomMsgID = UUID.randomUUID().toString()
        val randomStatus = TrafficNewsStatus.values().random()
        val randomMsg = MutableList(5) { UUID.randomUUID().toString() }.joinToString()

        return TrafficNews(
            msgId = randomMsgID,
            currentStatus = randomStatus,
            engShort = randomMsg,
            referenceDate = Date()
        )
    }
}