//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

fun Fragment.findNavController(currentId: Int): NavController? {
    try {
        val controller = NavHostFragment.findNavController(this)
        if (controller.currentDestination?.id != currentId) {
            return null
        }
        return controller
    } catch (e: Exception) {
        return null
    }
}