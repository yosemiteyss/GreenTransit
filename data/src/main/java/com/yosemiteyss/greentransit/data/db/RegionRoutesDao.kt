package com.yosemiteyss.greentransit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yosemiteyss.greentransit.data.dtos.RouteCodeLocalDto

/**
 * Created by kevin on 19/5/2021
 */

@Dao
interface RegionRoutesDao {

    @Query("SELECT * FROM route_codes WHERE region=:region ORDER BY code")
    suspend fun getRegionRoutes(region: String): List<RouteCodeLocalDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(localDtos: List<RouteCodeLocalDto>)

    @Query("DELETE FROM route_codes")
    suspend fun deleteAll()
}
