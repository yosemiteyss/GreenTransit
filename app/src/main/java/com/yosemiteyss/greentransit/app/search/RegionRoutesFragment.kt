package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.search.RegionRoutesViewModel.RegionRoutesViewModelFactory
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentRegionRoutesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kevin on 16/5/2021
 */

@AndroidEntryPoint
class RegionRoutesFragment : FullScreenDialogFragment(R.layout.fragment_region_routes) {

    private val binding: FragmentRegionRoutesBinding by viewBinding(FragmentRegionRoutesBinding::bind)
    private val navArgs: RegionRoutesFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RegionRoutesViewModelFactory

    private val viewModel: RegionRoutesViewModel by viewModels {
        RegionRoutesViewModel.provideFactory(viewModelFactory, navArgs.region)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController(R.id.regionRoutesFragment)?.navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toolbarTitle.collect {
                binding.toolbar.title = it
            }
        }

        // Setup recycler view
        val regionRoutesAdapter = RegionRoutesAdapter {

        }

        with(binding.routesRecyclerView) {
            adapter = regionRoutesAdapter.withLoadStateFooter(
                footer = PagingLoadStateAdapter { regionRoutesAdapter.retry() }
            )
        }

        regionRoutesAdapter.addLoadStateListener { loadStates ->
            binding.loadingProgressBar.showIf(loadStates.source.refresh is LoadState.Loading)
            loadStates.anyError()?.let {
                showShortToast(it.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.regionRoutes.collectLatest {
                regionRoutesAdapter.submitData(it)
            }
        }
    }
}