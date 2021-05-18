package com.yosemiteyss.greentransit.domain.models

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * Created by kevin on 18/5/2021
 */

class StopEtaResultTest {

    @Test
    fun `test correct time string`() {
        val randomHour = Random.nextInt(0, 23)
        val randomMin = Random.nextInt(0, 59)

        val result = mockk<StopEtaResult> {
            every { etaDate } returns Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, randomHour)
                set(Calendar.MINUTE, randomMin)
            }.time
        }

        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Hong_Kong")

        val expected = sdf.format(result.etaDate)

        assertEquals(expected, result.getEtaTimeString())
    }
}