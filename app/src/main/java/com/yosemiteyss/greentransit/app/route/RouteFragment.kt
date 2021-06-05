//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.route

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentRouteBinding
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.domain.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STOP_MARKER_ZOOM = 16f

@AndroidEntryPoint
class RouteFragment : FullScreenDialogFragment(R.layout.fragment_route) {

    private val binding: FragmentRouteBinding by viewBinding(FragmentRouteBinding::bind)
    private val navArgs: RouteFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RouteViewModel.RouteViewModelFactory

    private val viewModel: RouteViewModel by viewModels {
        RouteViewModel.provideFactory(viewModelFactory, navArgs.routeOption)
    }

    private val directionStopMarkers: MutableList<Marker> = mutableListOf()

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

        setupRouteInfo()
        setupRouteStopsList()
        setupMapFragment()
    }

    private fun setupRouteInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routeInfos.collect { res ->
                if (res is Resource.Error) {
                    showShortToast(res.message)
                }
            }
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

        // Expand directions button
        binding.switchDirectionButton.setOnClickListener {
            showRouteDirectionsBottomSheet()
        }
    }

    private fun setupRouteStopsList() {
        // Route stops
        val routeStopsAdapter = RouteStopsAdapter { stopId ->
            // Zoom to stop marker
            viewLifecycleOwner.lifecycleScope.launch {
                val stopLatLng = directionStopMarkers.firstOrNull { it.tag == stopId }?.position
                stopLatLng?.let {
                    getMapInstance().zoomTo(it, STOP_MARKER_ZOOM)
                }
            }
        }

        with(binding.stopsRecyclerView) {
            adapter = routeStopsAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stopsListModels.collect { res ->
                binding.loadingProgressBar.isVisible = res is Resource.Loading

                when (res) {
                    is Resource.Success -> routeStopsAdapter.routeStopsListModels = res.data
                    is Resource.Error -> showShortToast(res.message)
                    is Resource.Loading -> routeStopsAdapter.routeStopsListModels = emptyList()
                }
            }
        }
    }

    private fun setupMapFragment() {
        // Initialize map
        viewLifecycleOwner.lifecycleScope.launch {
            getMapInstance().run {
                with(uiSettings) {
                    isCompassEnabled = false
                    isMyLocationButtonEnabled = false
                    isMapToolbarEnabled = false
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

        // Add stop markers for a direction
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.directionStops.collect { stops ->
                if (stops.isEmpty()) return@collect

                getMapInstance().run {
                    directionStopMarkers.clear()
                    clear()

                    val markers = stops.map { stop ->
                        addMarker(
                            context = requireContext(),
                            position = LatLng(
                                stop.location.latitude, stop.location.longitude
                            ),
                            drawableRes = R.drawable.ic_stop,
                            tag = stop.stopId
                        )
                    }

                    directionStopMarkers.addAll(markers)

                    zoomToBoundMarkers(markers)
                }
            }
        }
    }

    private fun showRouteDirectionsBottomSheet() {
        RouteDirectionFragment.newInstance()
            .show(childFragmentManager, RouteDirectionFragment.TAG)
    }

    private suspend fun getMapInstance(): GoogleMap {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container_view)
            as SupportMapFragment
        return mapFragment.awaitMap()
    }
}