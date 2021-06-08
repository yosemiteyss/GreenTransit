//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.LocationSource.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentHomeBinding
import com.yosemiteyss.greentransit.app.main.MainViewModel
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val MAP_DEFAULT_ZOOM = 16f

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    LocationSource,
    GoogleMap.OnMarkerClickListener {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var onLocationChangedListener: OnLocationChangedListener? = null

    private var hasLoadedDefaultCoordinate: Boolean = false
    private val nearbyStopsMarkers: MutableList<Marker> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMapFragment()
        setupBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        nearbyStopsMarkers.clear()
    }

    override fun activate(listener: OnLocationChangedListener?) {
        onLocationChangedListener = listener
    }

    override fun deactivate() {
        onLocationChangedListener = null
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker == null) return false

        // Check if it is a stop marker
        if (marker in nearbyStopsMarkers) {
            marker.tag?.let {
                navigateToStop(stopId = it as Long)
            }
        }

        return true
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    private fun setupMapFragment() {
        // Map initialization
        viewLifecycleOwner.lifecycleScope.launch {
            getMapInstance().run {
                // Ui settings
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
                if (!hasLoadedDefaultCoordinate) {
                    moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            mainViewModel.defaultCoordinate.latitude,
                            mainViewModel.defaultCoordinate.longitude
                        ), 12f
                    ))
                    hasLoadedDefaultCoordinate = true
                }

                setOnMarkerClickListener(this@HomeFragment)
            }
        }

        // Attach location source if permission is granted
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.mapEnabled.collect { enabled ->
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
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.userLocation.collectLatest { userLocation ->
                    val location = Location("").apply {
                        latitude = userLocation.coordinate.latitude
                        longitude = userLocation.coordinate.longitude
                        bearing = userLocation.orientation
                    }
                    onLocationChangedListener?.onLocationChanged(location)
                }
            }
        }

        // Nearby stops marker
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.nearbyStops.collect { stops ->
                getMapInstance().run {
                    // Clear existing markers
                    nearbyStopsMarkers.clear()
                    this.clear()

                    // Add new group of markers
                    nearbyStopsMarkers.addAll(stops.map { stop ->
                        addMarker(
                            context = requireContext(),
                            position = LatLng(
                                stop.location.latitude, stop.location.longitude
                            ),
                            drawableRes = R.drawable.ic_stop,
                            tag = stop.id
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
            shapeAppearanceModel = ShapeAppearanceModel.builder(
                requireContext(),
                null,
                R.attr.shapeAppearanceLargeComponent,
                0
            ).build()

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
        val nearbyRoutesAdapter = NearbyRoutesAdapter(
            onRouteClicked = { routeId, routeCode ->
                findNavController(R.id.homeFragment)?.navigate(
                    HomeFragmentDirections.actionHomeFragmentToRouteFragment(
                        RouteOption(routeId = routeId, routeCode = routeCode)
                    )
                )
            }
        )

        with(binding.nearbyRoutesRecyclerView) {
            adapter = nearbyRoutesAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getHomeUiState(nearbyStops = mainViewModel.nearbyStops).collect { uiState ->
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
        getMapInstance().zoomAnimateTo(
            center = LatLng(location.coordinate.latitude, location.coordinate.longitude),
            level = MAP_DEFAULT_ZOOM
        )
    }

    private fun navigateToStop(stopId: Long) {
        val selectedStop = mainViewModel.nearbyStops.value.first { it.id == stopId }

        findNavController(R.id.homeFragment)?.navigate(
            HomeFragmentDirections.actionHomeFragmentToStopFragment(
                stopId = stopId,
                latitude = selectedStop.location.latitude.toFloat(),
                longitude = selectedStop.location.longitude.toFloat()
            )
        )
    }

    private suspend fun getMapInstance(): GoogleMap {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container_view)
            as SupportMapFragment
        return mapFragment.awaitMap()
    }
}