package com.yosemiteyss.greentransit.app.route

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentRouteBinding
import com.yosemiteyss.greentransit.domain.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 18/5/2021
 */

@AndroidEntryPoint
class RouteFragment : FullScreenDialogFragment(R.layout.fragment_route) {

    private val binding: FragmentRouteBinding by viewBinding(FragmentRouteBinding::bind)
    private val navArgs: RouteFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RouteViewModel.RouteViewModelFactory

    private val viewModel: RouteViewModel by viewModels {
        RouteViewModel.provideFactory(viewModelFactory, navArgs.routeOption)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutFullscreen(aboveNavBar = true)

        // Nav back button
        with(binding.navBackButton) {
            applySystemWindowInsetsMargin(applyTop = true)
            text = navArgs.routeOption.routeCode ?: navArgs.routeOption.routeRegionCode?.code

            setOnClickListener {
                findNavController(R.id.routeFragment)?.navigateUp()
            }
        }

        // Expand directions button
        binding.switchDirectionButton.setOnClickListener {
            showRouteDirectionsBottomSheet()
        }

        // Current direction
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentDirection.collect { direction ->
                binding.switchDirectionButton.isVisible = direction != null

                direction?.let {
                    binding.originTextView.text = it.origin
                    binding.destTextView.text = it.dest

                    with(binding.remarksTextView) {
                        isVisible = it.remarks != null
                        text = it.remarks
                        drawableFitVertical()
                    }
                }
            }
        }

        // Route info
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routeInfos.collect { res ->
                if (res is Resource.Error) {
                    showShortToast(res.message)
                }
            }
        }

        // Route stops
        val routeStopsAdapter = RouteStopsAdapter()

        with(binding.stopsRecyclerView) {
            adapter = routeStopsAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stopsListModels.collect { res ->
                binding.loadingProgressBar.isVisible = res is Resource.Loading

                when (res) {
                    is Resource.Success -> routeStopsAdapter.submitList(res.data)
                    is Resource.Error -> showShortToast(res.message)
                    is Resource.Loading -> routeStopsAdapter.submitList(emptyList())
                }
            }
        }
    }

    private fun showRouteDirectionsBottomSheet() {
        RouteDirectionFragment.newInstance()
            .show(childFragmentManager, RouteDirectionFragment.TAG)
    }
}