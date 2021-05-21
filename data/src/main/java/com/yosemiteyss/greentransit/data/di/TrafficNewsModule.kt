//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

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