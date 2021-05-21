//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.utils

import com.google.firebase.firestore.GeoPoint
import com.yosemiteyss.greentransit.domain.models.Coordinate

fun Coordinate.toGeoPoint(): GeoPoint {
    return GeoPoint(latitude, longitude)
}