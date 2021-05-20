package com.yosemiteyss.greentransit.data.dtos

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_TABLE

/**
 * Created by kevin on 19/5/2021
 */

@Entity(
    tableName = ROUTE_CODES_LOCAL_TABLE,
    primaryKeys = [ROUTE_CODES_LOCAL_DTO_CODE, ROUTE_CODES_LOCAL_DTO_REGION]
)
data class RouteCodeLocalDto(
    @ColumnInfo(name = ROUTE_CODES_LOCAL_DTO_CODE)
    val code: String,

    @ColumnInfo(name = ROUTE_CODES_LOCAL_DTO_REGION)
    val region: String
)