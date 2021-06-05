//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yosemiteyss.greentransit.app.databinding.FragmentRouteDirectionBinding
import com.yosemiteyss.greentransit.app.utils.parentViewModels
import com.yosemiteyss.greentransit.domain.models.RouteInfo
import com.yosemiteyss.greentransit.domain.states.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RouteDirectionFragment : BottomSheetDialogFragment() {

    private var binding: FragmentRouteDirectionBinding? = null
    private val routeViewModel: RouteViewModel by parentViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRouteDirectionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.run {
            val directionAdapter = RouteDirectionAdapter { routeId, routeSeq ->
                routeViewModel.onUpdateCurrentDirection(routeId, routeSeq)
                dismiss()
            }

            directionsRecyclerView.run {
                adapter = directionAdapter
            }

            viewLifecycleOwner.lifecycleScope.launch {
                routeViewModel.routeInfos.collect { res ->
                    if (res is Resource.Success) {
                        directionAdapter.submitList(buildRouteDirectionListModels(res.data))
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun buildRouteDirectionListModels(
        routeInfos: List<RouteInfo>
    ): List<RouteDirectionListModel> {
        val result = mutableListOf<RouteDirectionListModel>()

        routeInfos.forEach { info ->
            result.add(
                RouteDirectionListModel.RouteDirectionHeaderModel(
                    routeId = info.routeId,
                    description = info.description,
                    directionCount = info.directions.size
                )
            )

            info.directions.forEach { direction ->
                result.add(RouteDirectionListModel.RouteDirectionItemModel(
                    routeId = info.routeId,
                    routeSeq = direction.routeSeq,
                    origin = direction.origin,
                    dest = direction.dest,
                    remarks = direction.remarks
                ))
            }
        }

        return result
    }

    companion object {
        const val TAG = "RouteDirectionFragment"

        @JvmStatic
        fun newInstance(): RouteDirectionFragment = RouteDirectionFragment()
    }
}

