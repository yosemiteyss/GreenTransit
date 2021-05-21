//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.dtos

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_DTO_CODE
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_DTO_REGION
import com.yosemiteyss.greentransit.data.constants.Constants.ROUTE_CODES_LOCAL_TABLE

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