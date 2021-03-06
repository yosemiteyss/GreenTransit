//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentSearchBinding
import com.yosemiteyss.greentransit.app.route.RouteOption
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.domain.models.Region
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            searchEditText.applySystemWindowInsetsMargin(applyTop = true)
            loadingProgressBar.setVisibilityAfterHide(View.INVISIBLE)
            searchEditText.requestFocus()
        }

        // Set recycler view
        val searchAdapter = SearchAdapter(
            onRouteClicked = {
                findNavController(R.id.searchFragment)?.navigate(
                    SearchFragmentDirections.actionSearchFragmentToRouteFragment(
                        RouteOption(it)
                    )
                )
            },
            onRegionSelected = {
                navigateToRegionRoutes(it)
            }
        )

        with(binding.searchRecyclerView) {
            adapter = searchAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchUiState.collect { uiState ->
                binding.loadingProgressBar.showIf(uiState is SearchUiState.Loading)

                when (uiState) {
                    is SearchUiState.Success -> searchAdapter.submitList(uiState.data)
                    is SearchUiState.Idle -> searchAdapter.submitList(uiState.data)
                    is SearchUiState.Error -> showShortToast(uiState.message)
                    else -> Unit
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

            viewModel.onUpdateQuery(text.toString())
        }

        // Dismiss when clicking scrim region
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchRecyclerView.touchOutsideItemsFlow().collect {
                findNavController(R.id.searchFragment)?.navigateUp()
            }
        }
    }

    private fun navigateToRegionRoutes(region: Region) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToRegionRoutesFragment(region)
        )
    }
}