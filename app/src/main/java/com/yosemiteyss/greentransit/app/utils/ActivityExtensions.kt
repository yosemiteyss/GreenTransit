package com.yosemiteyss.greentransit.app.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.yosemiteyss.greentransit.BuildConfig

/**
 * Created by kevin on 4/29/21
 */

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