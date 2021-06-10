//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.stop

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.maps.android.ktx.awaitMap
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentStopBinding
import com.yosemiteyss.greentransit.app.main.MainViewModel
import com.yosemiteyss.greentransit.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAP_DEFAULT_ZOOM = 18f

@AndroidEntryPoint
class StopFragment : Fragment(R.layout.fragment_stop) {

    private val binding: FragmentStopBinding by viewBinding(FragmentStopBinding::bind)
    private val navArgs: StopFragmentArgs by navArgs()

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var stopViewModelFactory: StopViewModel.StopViewModelFactory

    private val stopViewModel: StopViewModel by viewModels {
        StopViewModel.provideFactory(stopViewModelFactory, navArgs.stopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTranslationZ(requireView(),
            resources.getDimensionPixelSize(R.dimen.elevation_large).toFloat())

        binding.stopViewPager.applySystemWindowInsetsMargin(applyBottom = true)

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
            stopViewModel.stopPages.collect { pages ->
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
            stopViewModel.stopUiState.collect { uiState ->
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
                    isRotateGesturesEnabled = false
                    isZoomGesturesEnabled = false
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
                ))
            }
        }

        // Navigate to Route
        viewLifecycleOwner.lifecycleScope.launch {
            stopViewModel.navigateToRoute.collect {
                findNavController(R.id.stopFragment)?.navigate(
                    StopFragmentDirections.actionStopFragmentToRouteFragment(it)
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

                zoomTo(stopLocation, MAP_DEFAULT_ZOOM)

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