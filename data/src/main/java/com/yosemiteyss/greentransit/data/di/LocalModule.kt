package com.yosemiteyss.greentransit.data.di

import android.content.Context
import androidx.room.Room
import com.yosemiteyss.greentransit.data.constants.Constants
import com.yosemiteyss.greentransit.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by kevin on 19/5/2021
 */

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.APP_DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}