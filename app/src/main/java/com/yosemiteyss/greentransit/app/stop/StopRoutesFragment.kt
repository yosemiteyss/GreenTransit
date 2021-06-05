//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.stop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentStopRoutesBinding
import com.yosemiteyss.greentransit.app.utils.parentViewModels
import com.yosemiteyss.greentransit.app.utils.showIf
import com.yosemiteyss.greentransit.app.utils.showShortToast
import com.yosemiteyss.greentransit.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopRoutesFragment : Fragment(R.layout.fragment_stop_routes) {

    private val binding: FragmentStopRoutesBinding by viewBinding(FragmentStopRoutesBinding::bind)
    private val stopViewModel: StopViewModel by parentViewModels()

    @Inject
    lateinit var viewModelFactory: StopRoutesViewModel.StopRoutesViewModelFactory

    private val stopRoutesViewModel: StopRoutesViewModel by viewModels {
        StopRoutesViewModel.provideFactory(viewModelFactory, stopViewModel.stopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            loadingProgressBar.setVisibilityAfterHide(View.GONE)
        }

        val routesAdapter = StopRoutesAdapter { routeId, routeCode ->
            stopViewModel.onNavigateToRoute(routeId, routeCode)
        }

        with(binding.routesRecyclerView) {
            adapter = routesAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            stopRoutesViewModel.stopRoutesUiState.collect { uiState ->
                binding.loadingProgressBar.showIf(uiState is StopRoutesUiState.Loading)

                when (uiState) {
                    is StopRoutesUiState.Success -> routesAdapter.submitList(uiState.data)
                    is StopRoutesUiState.Error -> {
                        routesAdapter.submitList(uiState.data)
                        showShortToast(uiState.message)
                    }
                    is StopRoutesUiState.Loading -> Unit
                }
            }
        }
    }
}