//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.domain.models

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by kevin on 10/5/2021
 */

class CoordinateTest {

    @Test
    fun `test calculate distance`() {
        val current = Coordinate(53.32055555555556, -1.7297222222222221)
        val from = Coordinate(53.31861111111111, -1.6997222222222223)
        val expected = 2004.3678382716137

        assertEquals(expected, current.distance(from), 0.0)
    }

    @Test
    fun `test decimal places`() {
        val expectedDp = 4
        val coordinate = Coordinate(53.32055555555556, -1.7297222222222221)
            .round(expectedDp)

        val latitudeDp = coordinate.latitude.toString().split('.')[1].length
        val longitudeDp = coordinate.longitude.toString().split('.')[1].length

        assertEquals(expectedDp, latitudeDp)
        assertEquals(expectedDp, longitudeDp)
    }
}