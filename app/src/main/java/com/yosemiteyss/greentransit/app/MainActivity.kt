package com.yosemiteyss.greentransit.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yosemiteyss.greentransit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GreenTransit_DayNight)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}