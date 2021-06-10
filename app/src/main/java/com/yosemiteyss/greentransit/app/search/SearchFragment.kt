//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentSearchBinding
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.domain.models.Region
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setLayoutFullscreen(aboveNavBar = true)
        ViewCompat.setTranslationZ(requireView(),
            resources.getDimensionPixelSize(R.dimen.elevation_large).toFloat())

        binding.searchRecyclerView.applySystemWindowInsetsMargin(applyBottom = true)

        with(binding) {
            clearTextButton.applySystemWindowInsetsMargin(applyTop = true)
            loadingProgressBar.setVisibilityAfterHide(View.INVISIBLE)
            searchEditText.applySystemWindowInsetsMargin(applyTop = true)
            showSoftKeyboard(searchEditText)
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
                hideSoftKeyboard()
            }
        }

        // Region chips
        binding.regionKlnChip.setOnClickListener {
            navigateToRegionRoutes(Region.KLN)
        }

        binding.regionHkiChip.setOnClickListener {
            navigateToRegionRoutes(Region.HKI)
        }

        binding.regionNtChip.setOnClickListener {
            navigateToRegionRoutes(Region.NT)
        }
    }

    override fun onStop() {
        super.onStop()
        hideSoftKeyboard()
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(requireContext(), InputMethodManager::class.java) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun navigateToRegionRoutes(region: Region) {
        findNavController(R.id.searchFragment)?.navigate(
            SearchFragmentDirections.actionSearchFragmentToRegionRoutesFragment(region)
        )
    }
}