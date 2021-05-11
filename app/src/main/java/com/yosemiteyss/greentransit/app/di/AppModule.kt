package com.yosemiteyss.greentransit.app.di

import com.yosemiteyss.greentransit.domain.di.CoroutineModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by kevin on 11/5/2021
 */

@Module(includes = [CoroutineModule::class])
@InstallIn(SingletonComponent::class)
object AppModule