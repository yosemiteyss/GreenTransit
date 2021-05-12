package com.yosemiteyss.greentransit.domain.repositories

import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.models.StopLocation
import java.util.*

/**
 * Created by kevin on 12/5/2021
 */

class FakeTransitRepositoryImpl : TransitRepository {

    private var throwNetworkError = false

    private val fakeStops: List<StopLocation> = MutableList(5) {
        createFakeStopLocation()
    }

    override suspend fun getNearbyStops(startHash: String, endHash: String): List<StopLocation> {
        if (throwNetworkError) throw Exception("Network error.")
        val random = Random()
        return listOf(
            StopLocation(
                stopId = random.nextLong(),
                location = Coordinate(
                    latitude = random.nextDouble(),
                    longitude = random.nextDouble()
                )
            )
        )
    }

    fun setNetworkError(throwError: Boolean) {
        this.throwNetworkError = throwError
    }

    private fun createFakeStopLocation(): StopLocation {
        val random = Random()
        return StopLocation(
            stopId = random.nextLong(),
            location = Coordinate(
                latitude = random.nextDouble(),
                longitude = random.nextDouble()
            )
        )
    }
}