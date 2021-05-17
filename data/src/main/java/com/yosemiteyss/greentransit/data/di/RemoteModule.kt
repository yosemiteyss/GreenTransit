package com.yosemiteyss.greentransit.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.yosemiteyss.greentransit.data.api.GMBService
import com.yosemiteyss.greentransit.data.api.GMBStopService
import com.yosemiteyss.greentransit.data.api.TrafficNewsService
import com.yosemiteyss.greentransit.data.constants.Constants.GMB_URL
import com.yosemiteyss.greentransit.data.constants.Constants.TRAFFIC_NEWS_URL
import com.yosemiteyss.greentransit.data.retrofit.GMBResponseConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

/**
 * Created by kevin on 11/5/2021
 */

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore.apply {
            firestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
            }
        }
    }

    @Singleton
    @Provides
    fun provideGMBService(): GMBService {
        return Retrofit.Builder()
            .baseUrl(GMB_URL)
            .addConverterFactory(GMBResponseConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GMBService::class.java)
    }

    @Singleton
    @Provides
    fun provideGMBStopService(): GMBStopService {
        return Retrofit.Builder()
            .baseUrl(GMB_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
            ))
            .build()
            .create(GMBStopService::class.java)
    }

    @Singleton
    @Provides
    fun provideTrafficNewsService(): TrafficNewsService {
        return Retrofit.Builder()
            .baseUrl(TRAFFIC_NEWS_URL)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build()
            .create(TrafficNewsService::class.java)
    }
}