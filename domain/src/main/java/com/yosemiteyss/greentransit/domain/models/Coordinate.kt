package com.yosemiteyss.greentransit.domain.models

import kotlin.math.*

/**
 * Created by kevin on 10/5/2021
 */

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