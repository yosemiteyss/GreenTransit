package com.yosemiteyss.greentransit.di

import com.yosemiteyss.greentransit.api.GMBService
import com.yosemiteyss.greentransit.constants.Constants.GMB_URL
import com.yosemiteyss.greentransit.retrofit.ResourceCallAdapterFactory
import com.yosemiteyss.greentransit.retrofit.SuccessResponseConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by kevin on 11/5/2021
 */

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun provideGMBService(): GMBService {
        return Retrofit.Builder()
            .baseUrl(GMB_URL)
            .addCallAdapterFactory(ResourceCallAdapterFactory())
            .addConverterFactory(SuccessResponseConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GMBService::class.java)
    }
}