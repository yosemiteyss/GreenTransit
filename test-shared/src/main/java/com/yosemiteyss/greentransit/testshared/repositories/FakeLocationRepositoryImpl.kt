package com.yosemiteyss.greentransit.testshared.repositories

import com.yosemiteyss.greentransit.domain.models.Coordinate
import com.yosemiteyss.greentransit.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

/**
 * Created by kevin on 6/6/2021
 */

class FakeLocationRepositoryImpl : LocationRepository {

    val fakeLocation = Coordinate(
        latitude = Random.nextDouble(),
        longitude = Random.nextDouble()
    )

    val fakeOrientation = Random.nextFloat()

    override fun getDeviceLocation(): Flow<Coordinate> = flowOf(fakeLocation)

    override fun getDeviceOrientation(): Flow<Float> = flowOf(fakeOrientation)
}