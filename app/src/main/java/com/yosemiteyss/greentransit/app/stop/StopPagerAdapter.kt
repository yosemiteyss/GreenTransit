//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.stop

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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
