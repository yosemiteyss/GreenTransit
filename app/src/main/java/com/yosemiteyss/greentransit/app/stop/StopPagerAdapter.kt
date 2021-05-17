package com.yosemiteyss.greentransit.app.stop

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by kevin on 17/5/2021
 */

class StopPagerAdapter(
    fragment: Fragment,
    val stopPages: List<StopPage>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = stopPages.size

    override fun createFragment(position: Int): Fragment = stopPages[position].fragment()
}

data class StopPage(
    @StringRes val titleRes: Int,
    val fragment: () -> Fragment
)
