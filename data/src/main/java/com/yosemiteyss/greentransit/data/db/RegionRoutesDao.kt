//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yosemiteyss.greentransit.data.dtos.RouteCodeLocalDto

@Dao
interface RegionRoutesDao {

    @Query("SELECT * FROM route_codes WHERE region=:region ORDER BY code")
    suspend fun getRegionRoutes(region: String): List<RouteCodeLocalDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(localDtos: List<RouteCodeLocalDto>)

    @Query("DELETE FROM route_codes")
    suspend fun deleteAll()
}
