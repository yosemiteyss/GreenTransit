//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.models

data class NearbyRoute(
    val id: Long,
    val seq: Int,
    val code: String,
    val origin: String,
    val dest: String,
    val region: Region,
)