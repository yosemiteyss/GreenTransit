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
        val expected = 2.0043678382716137

        assertEquals(expected, current.distance(from), 0.0)
    }
}