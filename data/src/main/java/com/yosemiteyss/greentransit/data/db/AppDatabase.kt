package com.yosemiteyss.greentransit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yosemiteyss.greentransit.data.dtos.RouteCodeLocalDto

/**
 * Created by kevin on 1/23/21
 */

@Database(
    entities = [RouteCodeLocalDto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun regionRoutesDao(): RegionRoutesDao
}