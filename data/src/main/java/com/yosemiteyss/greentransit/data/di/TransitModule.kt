package com.yosemiteyss.greentransit.data.di

import com.yosemiteyss.greentransit.data.mappers.TransitMapper
import com.yosemiteyss.greentransit.data.repositories.TransitRepositoryImpl
import com.yosemiteyss.greentransit.domain.repositories.TransitRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by kevin on 12/5/2021
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class TransitModule {

    @Singleton
    @Binds
    abstract fun bindsTransitRepository(
        transitRepositoryImpl: TransitRepositoryImpl
    ): TransitRepository

    companion object {
        @Singleton
        @Provides
        fun provideTransitMapper(): TransitMapper = TransitMapper()
    }
}