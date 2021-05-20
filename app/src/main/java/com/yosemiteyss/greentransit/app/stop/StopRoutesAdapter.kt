package com.yosemiteyss.greentransit.app.stop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.stop.StopRoutesListModel.StopRouteEmptyModel
import com.yosemiteyss.greentransit.app.stop.StopRoutesListModel.StopRouteItemModel
import com.yosemiteyss.greentransit.app.stop.StopRoutesViewHolder.StopRouteEmptyViewHolder
import com.yosemiteyss.greentransit.app.stop.StopRoutesViewHolder.StopRouteItemViewHolder
import com.yosemiteyss.greentransit.databinding.RoutesEmptyItemBinding
import com.yosemiteyss.greentransit.databinding.StopRouteListItemBinding
import com.yosemiteyss.greentransit.domain.models.StopRouteResult

/**
 * Created by kevin on 17/5/2021
 */

class StopRoutesAdapter(
    private val onRouteClicked: (routeId: Long, routeCode: String) -> Unit
) : ListAdapter<StopRoutesListModel, StopRoutesViewHolder>(StopRoutesListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopRoutesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.stop_route_list_item -> StopRouteItemViewHolder(
                StopRouteListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.routes_empty_item -> StopRouteEmptyViewHolder(
                RoutesEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: StopRoutesViewHolder, position: Int) {
        if (holder is StopRouteItemViewHolder) {
            bindStopRouteItem(
                binding = holder.binding,
                routeItemModel = getItem(position) as StopRouteItemModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is StopRouteItemModel -> R.layout.stop_route_list_item
            is StopRouteEmptyModel -> R.layout.routes_empty_item
        }
    }

    private fun bindStopRouteItem(
        binding: StopRouteListItemBinding,
        routeItemModel: StopRouteItemModel
    ) = binding.run {
        routeCodeTextView.text = routeItemModel.stopRouteResult.routeCode.code
        routeStopNameTextView.text = root.context.getString(
            R.string.stop_routes_stop_name,
            routeItemModel.stopRouteResult.stopRoute.stopSeq,
            routeItemModel.stopRouteResult.stopRoute.name
        )

        root.setOnClickListener {
            onRouteClicked(
                routeItemModel.stopRouteResult.stopRoute.routeId,
                routeItemModel.stopRouteResult.routeCode.code
            )
        }
    }
}

sealed class StopRoutesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class StopRouteItemViewHolder(
        val binding: StopRouteListItemBinding
    ) : StopRoutesViewHolder(binding.root)

    class StopRouteEmptyViewHolder(
        val binding: RoutesEmptyItemBinding
    ) : StopRoutesViewHolder(binding.root)
}

sealed class StopRoutesListModel {
    data class StopRouteItemModel(
        val stopRouteResult: StopRouteResult
    ) : StopRoutesListModel()

    object StopRouteEmptyModel : StopRoutesListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<StopRoutesListModel>() {
            override fun areItemsTheSame(
                oldItem: StopRoutesListModel,
                newItem: StopRoutesListModel
            ): Boolean = when {
                oldItem is StopRouteItemModel && newItem is StopRouteItemModel ->
                    oldItem.stopRouteResult.stopRoute.routeId ==
                        newItem.stopRouteResult.stopRoute.routeId
                oldItem is StopRouteEmptyModel && newItem is StopRouteEmptyModel ->
                    true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: StopRoutesListModel,
                newItem: StopRoutesListModel
            ): Boolean = when {
                oldItem is StopRouteItemModel && newItem is StopRouteItemModel ->
                    oldItem.stopRouteResult == newItem.stopRouteResult
                oldItem is StopRouteEmptyModel && newItem is StopRouteEmptyModel ->
                    true
                else -> false
            }
        }
    }
}