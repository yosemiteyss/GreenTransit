//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentRegionRoutesBinding
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.search.RegionRoutesViewModel.RegionRoutesViewModelFactory
import com.yosemiteyss.greentransit.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RegionRoutesFragment : Fragment(R.layout.fragment_region_routes) {

    private val binding: FragmentRegionRoutesBinding by viewBinding(FragmentRegionRoutesBinding::bind)
    private val navArgs: RegionRoutesFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RegionRoutesViewModelFactory

    private val viewModel: RegionRoutesViewModel by viewModels {
        RegionRoutesViewModel.provideFactory(viewModelFactory, navArgs.region)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBarLayout.applySystemWindowInsetsPadding(applyTop = true)
        binding.routesRecyclerView.applySystemWindowInsetsMargin(applyBottom = true)

        with(binding.loadingProgressBar) {
            setVisibilityAfterHide(View.INVISIBLE)
            showAnimationBehavior = LinearProgressIndicator.SHOW_INWARD
            hideAnimationBehavior = LinearProgressIndicator.SHOW_OUTWARD
        }

        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toolbarTitle.collect {
                binding.toolbar.title = it
            }
        }

        // Setup recycler view
        val regionRoutesAdapter = RegionRoutesAdapter { routeRegionCode ->
            findNavController().navigate(
                RegionRoutesFragmentDirections.actionRegionRoutesFragmentToRouteFragment(
                    RouteOption(routeRegionCode)
                )
            )
        }

        with(binding.routesRecyclerView) {
            adapter = regionRoutesAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routesUiState.collect { uiState ->
                binding.loadingProgressBar.showIf(uiState is RegionRoutesUiState.Loading)

                if (uiState is RegionRoutesUiState.Success) {
                    regionRoutesAdapter.submitList(uiState.data)
                } else if (uiState is RegionRoutesUiState.Error) {
                    regionRoutesAdapter.submitList(uiState.data)
                    showShortToast(uiState.message)
                }
            }
        }
    }
}