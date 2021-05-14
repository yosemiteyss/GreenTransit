package com.yosemiteyss.greentransit.data.di

import com.yosemiteyss.greentransit.data.mappers.TrafficNewsMapper
import com.yosemiteyss.greentransit.data.repositories.TrafficNewsRepositoryImpl
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by kevin on 14/5/2021
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class TrafficNewsModule {

    @Singleton
    @Binds
    abstract fun bindsTrafficNewsRepository(
        trafficNewsRepositoryImpl: TrafficNewsRepositoryImpl
    ): TrafficNewsRepository

    companion object {
        @Singleton
        @Provides
        fun provideTrafficNewsMapper(): TrafficNewsMapper = TrafficNewsMapper()
    }
}