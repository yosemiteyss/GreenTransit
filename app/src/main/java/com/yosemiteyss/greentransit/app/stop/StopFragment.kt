package com.yosemiteyss.greentransit.app.stop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.viewBinding
import com.yosemiteyss.greentransit.databinding.FragmentStopBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 17/5/2021
 */

@AndroidEntryPoint
class StopFragment : Fragment(R.layout.fragment_stop) {

    private val binding: FragmentStopBinding by viewBinding(FragmentStopBinding::bind)
    private val navArgs: StopFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: StopViewModel.StopViewModelFactory

    private val viewModel: StopViewModel by viewModels {
        StopViewModel.provideFactory(viewModelFactory, navArgs.stopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup view pager
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stopPages.collect { pages ->
                if (pages.isEmpty()) return@collect

                val stopPagerAdapter = StopPagerAdapter(
                    fragment = this@StopFragment,
                    stopPages = pages
                )

                binding.stopViewPager.adapter = stopPagerAdapter

                TabLayoutMediator(
                    binding.stopTabLayout,
                    binding.stopViewPager
                ) { tab, position ->
                    tab.setText(stopPagerAdapter.stopPages[position].titleRes)
                }.attach()
            }
        }
    }
}