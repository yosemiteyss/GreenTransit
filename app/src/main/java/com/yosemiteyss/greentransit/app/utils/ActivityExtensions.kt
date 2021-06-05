//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.yosemiteyss.greentransit.app.BuildConfig

fun AppCompatActivity.setLayoutFullscreen(aboveNavBar: Boolean = false) {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (aboveNavBar) {
        window.decorView.findViewById<ViewGroup>(android.R.id.content)
            .applySystemWindowInsetsMargin(applyBottom = true)
    }
}

fun AppCompatActivity.launchAppSettings() {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    startActivity(intent)
}