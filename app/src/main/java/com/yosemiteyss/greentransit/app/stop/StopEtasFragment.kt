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
import com.yosemiteyss.greentransit.app.databinding.FragmentStopEtasBinding
import com.yosemiteyss.greentransit.app.utils.parentViewModels
import com.yosemiteyss.greentransit.app.utils.showShortToast
import com.yosemiteyss.greentransit.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopEtasFragment : Fragment(R.layout.fragment_stop_etas) {

    private val binding: FragmentStopEtasBinding by viewBinding(FragmentStopEtasBinding::bind)
    private val stopViewModel: StopViewModel by parentViewModels()

    @Inject
    lateinit var viewModelFactory: StopEtasViewModel.StopEtasModelFactory

    private val stopEtasViewModel: StopEtasViewModel by viewModels {
        StopEtasViewModel.provideFactory(viewModelFactory, stopViewModel.stopId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etasAdapter = StopEtasAdapter { routeId, routeCode ->
            stopViewModel.onNavigateToRoute(routeId, routeCode)
        }

        with(binding.etasRecyclerView) {
            adapter = etasAdapter
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            stopEtasViewModel.stopEtasUiState.collect { uiState ->
                stopViewModel.onShowLoading(uiState is StopEtasUiState.Loading)

                when (uiState) {
                    is StopEtasUiState.Success -> {
                        etasAdapter.submitList(uiState.data)
                        showShortToast("Updated.")
                    }
                    is StopEtasUiState.Error -> {
                        etasAdapter.submitList(uiState.data)
                        showShortToast(uiState.message)
                    }
                    StopEtasUiState.Loading -> Unit
                }
            }
        }
    }
}