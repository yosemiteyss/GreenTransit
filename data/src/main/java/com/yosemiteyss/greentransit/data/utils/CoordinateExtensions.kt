package com.yosemiteyss.greentransit.data.utils

import com.google.firebase.firestore.GeoPoint
import com.yosemiteyss.greentransit.domain.models.Coordinate

/**
 * Created by kevin on 12/5/2021
 */

fun Coordinate.toGeoPoint(): GeoPoint {
    return GeoPoint(latitude, longitude)
}