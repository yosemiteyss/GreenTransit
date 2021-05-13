package com.yosemiteyss.greentransit.app.main

import app.cash.turbine.test
import com.yosemiteyss.greentransit.testshared.TestCoroutineRule
import com.yosemiteyss.greentransit.testshared.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kevin on 12/5/2021
 */

class MainViewModelTest {

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    @Before
    fun init() {

    }

    @Test
    fun `test enable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = MainViewModel()
        mainViewModel.onEnableMap(true)

        mainViewModel.enableMap.test {
            assertTrue(expectItem())
        }
    }

    @Test
    fun `test disable map`() = coroutineRule.runBlockingTest {
        val mainViewModel = MainViewModel()
        mainViewModel.onEnableMap(false)

        mainViewModel.enableMap.test {
            assertFalse(expectItem())
        }
    }
}