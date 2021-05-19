package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentSearchBinding
import com.yosemiteyss.greentransit.domain.models.RouteRegion
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kevin on 12/5/2021
 */

@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setStyle(STYLE_NORMAL, R.style.ThemeOverlay_GreenTransit_Dialog_Fullscreen_DayNight_Search)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutFullscreen(aboveNavBar = true)

        with(binding) {
            clearTextButton.applySystemWindowInsetsMargin(applyTop = true)
            loadingProgressBar.setVisibilityAfterHide(View.INVISIBLE)
            searchEditText.applySystemWindowInsetsMargin(applyTop = true)
            searchEditText.requestFocus()
        }

        // Set recycler view
        val searchAdapter = SearchAdapter { routeRegionCode ->
            findNavController(R.id.searchFragment)?.navigate(
                SearchFragmentDirections.actionSearchFragmentToRouteFragment(
                    RouteOption(routeRegionCode = routeRegionCode)
                )
            )
        }

        with(binding.searchRecyclerView) {
            adapter = searchAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchUiState.collect { uiState ->
                binding.loadingProgressBar.showIf(uiState is SearchUiState.Loading)

                if (uiState is SearchUiState.Success) {
                    searchAdapter.submitList(uiState.data)
                } else if (uiState is SearchUiState.Error) {
                    showShortToast(uiState.message)
                }
            }
        }

        // Clear text
        binding.clearTextButton.setOnClickListener {
            if (binding.searchEditText.text.isEmpty()) {
                findNavController(R.id.searchFragment)?.navigateUp()
            } else {
                binding.searchEditText.text.clear()
            }
        }

        // Observer search input
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            // Hide chip group when the user is inputting
            binding.regionsScrollView.isVisible = text.toString().isBlank()
            viewModel.onUpdateQuery(text.toString())
        }

        // Dismiss when clicking scrim region
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchRecyclerView.touchOutsideItemsFlow().collect {
                findNavController(R.id.searchFragment)?.navigateUp()
            }
        }

        // Region chips
        binding.regionKlnChip.setOnClickListener {
            navigateToRegionRoutes(RouteRegion.KLN)
        }

        binding.regionHkiChip.setOnClickListener {
            navigateToRegionRoutes(RouteRegion.HKI)
        }

        binding.regionNtChip.setOnClickListener {
            navigateToRegionRoutes(RouteRegion.NT)
        }
    }

    private fun navigateToRegionRoutes(routeRegion: RouteRegion) {
        findNavController(R.id.searchFragment)?.navigate(
            SearchFragmentDirections.actionSearchFragmentToRegionRoutesFragment(routeRegion)
        )
    }
}