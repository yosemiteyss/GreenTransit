package com.yosemiteyss.greentransit.domain.usecases.location

import com.yosemiteyss.greentransit.domain.di.MainDispatcher
import com.yosemiteyss.greentransit.domain.models.Location
import com.yosemiteyss.greentransit.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by kevin on 6/6/2021
 */

class GetDeviceLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {

    operator fun invoke(): Flow<Location> = flow {
        val locationFlow = locationRepository.getDeviceLocation()
        val orientationFlow = locationRepository.getDeviceOrientation()

        emitAll(locationFlow.combine(orientationFlow, ::Location))
    }
        .flowOn(coroutineDispatcher)
}