package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import java.util.*

class FakeTrafficNewsRepositoryImpl : TrafficNewsRepository {

    private var throwNetworkError = false
    private var dontMakeData = false

    override suspend fun getTrafficNews(): List<TrafficNews> {
        if (throwNetworkError) throw Exception("Network error.")
        if (dontMakeData) {
            return emptyList()
        }
        else {
            return MutableList(5) {
                createFakeTrafficNews()
            }
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
        val randomMsg = UUID.randomUUID().toString()
        return TrafficNews(
            msgId = randomMsgID,
            currentStatus = randomStatus,
            engShort = randomMsg,
            referenceDate = Date()
        )
    }
}