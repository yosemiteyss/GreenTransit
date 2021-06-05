package com.yosemiteyss.greentransit.data.di

import android.content.Context
import android.hardware.SensorManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yosemiteyss.greentransit.data.repositories.LocationRepositoryImpl
import com.yosemiteyss.greentransit.domain.repositories.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by kevin on 6/6/2021
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Singleton
    @Binds
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    companion object {
        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }

        @Provides
        @Singleton
        fun provideSensorManager(
            @ApplicationContext context: Context
        ): SensorManager {
            return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }
    }
}