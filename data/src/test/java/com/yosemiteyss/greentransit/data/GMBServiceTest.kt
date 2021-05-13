package com.yosemiteyss.greentransit.data

import com.yosemiteyss.greentransit.data.api.GMBService
import com.yosemiteyss.greentransit.data.constants.Constants
import com.yosemiteyss.greentransit.data.retrofit.SuccessResponseConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kevin on 13/5/2021
 */

class GMBServiceTest {

    private lateinit var gmbService: GMBService

    @Before
    fun init() {
        gmbService = Retrofit.Builder()
            .baseUrl(Constants.GMB_URL)
            .addConverterFactory(SuccessResponseConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GMBService::class.java)
    }

    @Test
    fun `test`() = runBlocking {
        val results = gmbService.getStopEta(20003337)
        println(results)
    }
}