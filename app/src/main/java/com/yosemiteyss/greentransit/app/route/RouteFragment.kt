//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.route

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentRouteBinding
import com.yosemiteyss.greentransit.app.main.MainViewModel
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.domain.models.StopInfo
import com.yosemiteyss.greentransit.domain.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val STOP_MARKER_ZOOM = 16f

@AndroidEntryPoint
class RouteFragment : Fragment(R.layout.fragment_route) {

    private val binding: FragmentRouteBinding by viewBinding(FragmentRouteBinding::bind)
    private val navArgs: RouteFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var routeViewModelFactory: RouteViewModel.RouteViewModelFactory
    private val routeViewModel: RouteViewModel by viewModels {
        RouteViewModel.provideFactory(routeViewModelFactory, navArgs.routeOption)
    }

    private val directionStopMarkers: MutableList<Marker> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(requireView(),
            resources.getDimensionPixelSize(R.dimen.elevation_large).toFloat())

        binding.stopsRecyclerView.applySystemWindowInsetsMargin(applyBottom = true)

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
            routeViewModel.routeInfos.collect { res ->
                if (res is Resource.Error) {
                    showShortToast(res.message)
                }
            }
        }

        // Current direction
        viewLifecycleOwner.lifecycleScope.launch {
            routeViewModel.currentDirection.collect { direction ->
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
            routeViewModel.stopsListModels.collect { res ->
                binding.loadingProgressBar.showIf(res is Resource.Loading)

                when (res) {
                    is Resource.Success -> routeStopsAdapter.submitList(res.data)
                    is Resource.Error -> showShortToast(res.message)
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    private fun setupMapFragment() {
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

                // Move to default location
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            mainViewModel.initialCoordinate.latitude,
                            mainViewModel.initialCoordinate.longitude
                        ), 12f
                    )
                )
            }
        }

        // Add stop markers for a direction
        viewLifecycleOwner.lifecycleScope.launch {
            routeViewModel.directionStopsInfos.collect { res ->
                when (res) {
                    is Resource.Success -> insertStopsMarkers(res.data)
                    is Resource.Error -> showShortToast(res.message)
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    private suspend fun insertStopsMarkers(stopsInfos: List<StopInfo>) {
        if (stopsInfos.isNotEmpty()) {
            getMapInstance().run {
                directionStopMarkers.clear()
                clear()

                val markers = stopsInfos.map { info ->
                    addMarker(
                        context = requireContext(),
                        position = LatLng(info.location.latitude, info.location.longitude),
                        drawableRes = R.drawable.ic_stop,
                        tag = info.stopId
                    )
                }

                directionStopMarkers.addAll(markers)

                zoomToBoundMarkers(markers)
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