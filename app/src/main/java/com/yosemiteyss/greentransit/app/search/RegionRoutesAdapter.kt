package com.yosemiteyss.greentransit.app.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesEmptyModel
import com.yosemiteyss.greentransit.app.search.RegionRoutesListModel.RegionRoutesItemModel
import com.yosemiteyss.greentransit.app.search.RegionRoutesViewHolder.RegionRoutesEmptyViewHolder
import com.yosemiteyss.greentransit.app.search.RegionRoutesViewHolder.RegionRoutesItemViewHolder
import com.yosemiteyss.greentransit.databinding.RegionRoutesListItemBinding
import com.yosemiteyss.greentransit.databinding.RoutesEmptyItemBinding
import com.yosemiteyss.greentransit.domain.models.RouteCode

/**
 * Created by kevin on 17/5/2021
 */

class RegionRoutesAdapter(
    private val onRouteClicked: (routeCode: RouteCode) -> Unit
) : ListAdapter<RegionRoutesListModel, RegionRoutesViewHolder>(RegionRoutesListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionRoutesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.region_routes_list_item -> RegionRoutesItemViewHolder(
                RegionRoutesListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.routes_empty_item -> RegionRoutesEmptyViewHolder(
                RoutesEmptyItemBinding.inflate(inflater, parent, false)

            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RegionRoutesViewHolder, position: Int) {
        when (holder) {
            is RegionRoutesItemViewHolder -> bindRegionRouteItemModel(
                binding = holder.binding,
                itemModel = getItem(position) as RegionRoutesItemModel
            )
            is RegionRoutesEmptyViewHolder -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RegionRoutesItemModel -> R.layout.region_routes_list_item
            is RegionRoutesEmptyModel -> R.layout.routes_empty_item
            else -> throw IllegalStateException("Unknown view type.")
        }
    }

    private fun bindRegionRouteItemModel(
        binding: RegionRoutesListItemBinding,
        itemModel: RegionRoutesItemModel
    ) = binding.run {
        routeCodeTextView.text = itemModel.routeCode.code

        root.setOnClickListener {
            onRouteClicked(itemModel.routeCode)
        }
    }
}

sealed class RegionRoutesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class RegionRoutesItemViewHolder(
        val binding: RegionRoutesListItemBinding
    ) : RegionRoutesViewHolder(binding.root)

    class RegionRoutesEmptyViewHolder(
        val binding: RoutesEmptyItemBinding
    ) : RegionRoutesViewHolder(binding.root)
}

sealed class RegionRoutesListModel {
    data class RegionRoutesItemModel(
        val routeCode: RouteCode
    ) : RegionRoutesListModel()

    object RegionRoutesEmptyModel : RegionRoutesListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<RegionRoutesListModel>() {
            override fun areItemsTheSame(
                oldItem: RegionRoutesListModel,
                newItem: RegionRoutesListModel
            ): Boolean = when {
                oldItem is RegionRoutesItemModel && newItem is RegionRoutesItemModel ->
                    oldItem == newItem
                oldItem is RegionRoutesEmptyModel && newItem is RegionRoutesEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: RegionRoutesListModel,
                newItem: RegionRoutesListModel
            ): Boolean  = when {
                oldItem is RegionRoutesItemModel && newItem is RegionRoutesItemModel ->
                    oldItem == newItem
                oldItem is RegionRoutesEmptyModel && newItem is RegionRoutesEmptyModel -> true
                else -> false
            }
        }
    }
}