package com.yosemiteyss.greentransit.app.stop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentStopBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 17/5/2021
 */

private const val MAP_DEFAULT_ZOOM = 18f

@AndroidEntryPoint
class StopFragment : FullScreenDialogFragment(R.layout.fragment_stop) {

    private val binding: FragmentStopBinding by viewBinding(FragmentStopBinding::bind)
    private val navArgs: StopFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: StopViewModel.StopViewModelFactory

    private val viewModel: StopViewModel by viewModels {
        StopViewModel.provideFactory(viewModelFactory, navArgs.stopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutFullscreen(aboveNavBar = true)

        with(binding.navBackButton) {
            applySystemWindowInsetsMargin(applyTop = true)
            setOnClickListener {
                findNavController(R.id.stopFragment)?.navigateUp()
            }
        }

        // Set coordinate title
        binding.stopCoordinateTextView.text = getString(
            R.string.stop_coordinates,
            navArgs.latitude,
            navArgs.longitude
        )

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

        // Setup stop info
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stopUiState.collect { uiState ->
                if (uiState == null) return@collect

                when (uiState) {
                    is StopUiState.Success -> setupUI(uiState)
                    is StopUiState.Error -> showShortToast(uiState.message)
                    StopUiState.Loading -> Unit
                }
            }
        }

        // Setup map
        viewLifecycleOwner.lifecycleScope.launch {
            getMapInstance().run {
                with(uiSettings) {
                    isCompassEnabled = false
                    isMyLocationButtonEnabled = false
                    isMapToolbarEnabled = false
                    isScrollGesturesEnabled = false
                }

                // Switch map to night mode
                if (requireContext().isNightModeOn()) {
                    val nightStyle = MapStyleOptions.loadRawResourceStyle(
                        requireContext(), R.raw.night_map_style
                    )

                    setMapStyle(nightStyle)
                }
            }
        }

        // Navigate to Route
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToRoute.collect {
                findNavController(R.id.stopFragment)?.navigate(
                    StopFragmentDirections.actionStopFragmentToRouteFragment(
                        RouteOption(routeId = it.first, routeCode = it.second)
                    )
                )
            }
        }
    }

    private fun setupUI(uiState: StopUiState.Success) = with(binding) {
        // Show remarks if the stop is disabled
        stopRemarkTextView.text = uiState.data.remarks ?: getString(R.string.stop_default_remark)

        // Move camera to stop location
        viewLifecycleOwner.lifecycleScope.launch {
            getMapInstance().run {
                val stopLocation = LatLng(
                    uiState.data.location.latitude,
                    uiState.data.location.longitude
                )

                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(stopLocation, MAP_DEFAULT_ZOOM)
                )

                addMarker(
                    context = requireContext(),
                    position = stopLocation,
                    drawableRes = R.drawable.ic_stop
                )
            }
        }
    }

    private suspend fun getMapInstance(): GoogleMap {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container_view)
            as SupportMapFragment
        return mapFragment.awaitMap()
    }
}