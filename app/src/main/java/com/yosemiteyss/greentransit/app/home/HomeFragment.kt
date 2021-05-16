package com.yosemiteyss.greentransit.app.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.LocationSource.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.main.MainViewModel
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Created by kevin on 12/5/2021
 */

private const val MAP_DEFAULT_ZOOM = 16f

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), LocationSource {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var onLocationChangedListener: OnLocationChangedListener? = null

    private var nearbyStopMarkers: MutableList<Marker> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMapFragment()
        setupBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nearbyStopMarkers.clear()
    }

    override fun activate(listener: OnLocationChangedListener?) {
        onLocationChangedListener = listener
    }

    override fun deactivate() {
        onLocationChangedListener = null
    }

    @SuppressLint("MissingPermission")
    private fun setupMapFragment() {
        // Map initialization
        viewLifecycleOwner.lifecycleScope.launch {
            getMapInstance().run {
                // Ui settings
                with(uiSettings) {
                    isCompassEnabled = false
                    isMyLocationButtonEnabled = false
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

        // Attach location source if permission is granted
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.enableMap.collect { enabled ->
                if (enabled) {
                    getMapInstance().run {
                        isMyLocationEnabled = true
                        setLocationSource(this@HomeFragment)
                    }
                }
            }
        }

        // Zoom to current location when start
        viewLifecycleOwner.lifecycleScope.launch {
            zoomToCurrentLocation()
        }

        // Pass current location to location source
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.userLocation.collectLatest {
                onLocationChangedListener?.onLocationChanged(it)
            }
        }

        // Nearby stops marker
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.nearbyStops.collect { stops ->
                Log.d("Home", "${stops.map { it.routeId }}")
                getMapInstance().run {
                    nearbyStopMarkers.clear()
                    nearbyStopMarkers.addAll(stops.map { stop ->
                        addMarker(
                            context = requireContext(),
                            position = LatLng(
                                stop.location.latitude, stop.location.longitude
                            ),
                            drawableRes = R.drawable.ic_stop
                        )
                    })
                }
            }
        }

        // My location button
        binding.homeSearchBarLayout.myLocationButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                zoomToCurrentLocation()
            }
        }
    }

    private fun setupBottomSheet() {
        binding.bottomSheetLayout.background = MaterialShapeDrawable(
            requireContext(),
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            fillColor = ColorStateList.valueOf(
                requireContext().themeColor(R.attr.colorSurface)
            )
            elevation = resources.getDimension(R.dimen.elevation_xmedium)
            initializeElevationOverlay(requireContext())
        }

        // Navigate to SearchFragment
        binding.homeSearchBarLayout.root.setOnClickListener {
            findNavController(R.id.homeFragment)?.navigate(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            )
        }

        // Bottom sheet top margin
        binding.bottomSheetCoordinatorLayout.applySystemWindowInsetsMargin(applyTop = true)

        // Get nearby routes
        val nearbyRoutesAdapter = NearbyRoutesAdapter {
            // on click
        }

        with(binding.nearbyRoutesRecyclerView) {
            adapter = nearbyRoutesAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getNearbyRouteEtas(nearbyStops = mainViewModel.nearbyStops).collect { uiState ->
                binding.loadingProgressBar.showIf(uiState is HomeUiState.Loading)

                if (uiState is HomeUiState.Success) {
                    nearbyRoutesAdapter.submitList(uiState.data)
                } else if (uiState is HomeUiState.Error) {
                    showShortToast(uiState.message)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.nearbyRoutesCount.collect {
                binding.nearbyRoutesCountTextView.text = getString(R.string.nearby_routes_count, it)
            }
        }
    }

    private suspend fun zoomToCurrentLocation() {
        val location = mainViewModel.userLocation.first()
        getMapInstance().animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                MAP_DEFAULT_ZOOM
            )
        )
    }

    private suspend fun getMapInstance(): GoogleMap {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container_view)
            as SupportMapFragment
        return mapFragment.awaitMap()
    }
}