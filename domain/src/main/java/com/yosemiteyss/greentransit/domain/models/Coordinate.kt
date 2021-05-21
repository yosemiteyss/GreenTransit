//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.models

import kotlin.math.*

private const val R = 6371

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

/**
 * Calculate the distance of two points using Haversine formula.
 * @return distance between two points in meters.
 */
fun Coordinate.distance(from: Coordinate): Double {
    val fromLat = Math.toRadians(from.latitude)
    val fromLng = Math.toRadians(from.longitude)
    val currLat = Math.toRadians(latitude)
    val currLng = Math.toRadians(longitude)

    val dLat = currLat - fromLat
    val dLng = currLng - fromLng

    val a = sin(dLat / 2).pow(2) +
        cos(fromLat) * cos(currLat) *
        sin(dLng / 2).pow(2)

    val c = 2 * asin(sqrt(a))

    return c * R * 1000
}