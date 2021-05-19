package com.yosemiteyss.greentransit.app.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.route.RouteDirectionListModel.*
import com.yosemiteyss.greentransit.app.route.RouteDirectionViewHolder.*
import com.yosemiteyss.greentransit.databinding.RouteDirectionHeaderItemBinding
import com.yosemiteyss.greentransit.databinding.RouteDirectionListItemBinding
import com.yosemiteyss.greentransit.databinding.RoutesEmptyItemBinding

/**
 * Created by kevin on 19/5/2021
 */

class RouteDirectionAdapter(
    private val onDirectionSelected: (routeId: Long, routeSeq: Int) -> Unit
) : ListAdapter<RouteDirectionListModel, RouteDirectionViewHolder>(RouteDirectionListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteDirectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.route_direction_header_item -> RouteDirectionHeaderViewHolder(
                RouteDirectionHeaderItemBinding.inflate(inflater, parent, false)
            )
            R.layout.route_direction_list_item -> RouteDirectionItemViewHolder(
                RouteDirectionListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.routes_empty_item -> RouteDirectionEmptyViewHolder(
                RoutesEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RouteDirectionViewHolder, position: Int) {
        when (holder) {
            is RouteDirectionHeaderViewHolder -> bindDirectionHeader(
                binding = holder.binding,
                headerModel = getItem(position) as RouteDirectionHeaderModel
            )
            is RouteDirectionItemViewHolder -> bindDirectionItem(
                binding = holder.binding,
                itemModel = getItem(position) as RouteDirectionItemModel
            )
            is RouteDirectionEmptyViewHolder -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RouteDirectionHeaderModel -> R.layout.route_direction_header_item
            is RouteDirectionItemModel -> R.layout.route_direction_list_item
            RouteDirectionEmptyModel -> R.layout.routes_empty_item
        }
    }

    private fun bindDirectionHeader(
        binding: RouteDirectionHeaderItemBinding,
        headerModel: RouteDirectionHeaderModel
    ) = binding.run {
        headerTextView.text = root.context.getString(
            R.string.route_header,
            headerModel.description,
            headerModel.directionCount
        )
    }

    private fun bindDirectionItem(
        binding: RouteDirectionListItemBinding,
        itemModel: RouteDirectionItemModel
    ) = binding.run {
        directionTextView.text = root.context.getString(
            R.string.route_direction,
            itemModel.origin,
            itemModel.dest
        )

        with(remarksTextView) {
            isVisible = itemModel.remarks != null
            text = itemModel.remarks
        }

        root.setOnClickListener {
            onDirectionSelected(itemModel.routeId, itemModel.routeSeq)
        }
    }
}

sealed class RouteDirectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class RouteDirectionHeaderViewHolder(
        val binding: RouteDirectionHeaderItemBinding
    ) : RouteDirectionViewHolder(binding.root)

    class RouteDirectionItemViewHolder(
        val binding: RouteDirectionListItemBinding
    ) : RouteDirectionViewHolder(binding.root)

    class RouteDirectionEmptyViewHolder(
        val binding: RoutesEmptyItemBinding
    ) : RouteDirectionViewHolder(binding.root)
}

sealed class RouteDirectionListModel {
    data class RouteDirectionHeaderModel(
        val routeId: Long,
        val description: String,
        val directionCount: Int
    ) : RouteDirectionListModel()

    data class RouteDirectionItemModel(
        val routeId: Long,
        val routeSeq: Int,
        val origin: String,
        val dest: String,
        val remarks: String?
    ) : RouteDirectionListModel()

    object RouteDirectionEmptyModel : RouteDirectionListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<RouteDirectionListModel>() {
            override fun areItemsTheSame(
                oldItem: RouteDirectionListModel,
                newItem: RouteDirectionListModel
            ): Boolean = when {
                oldItem is RouteDirectionHeaderModel && newItem is RouteDirectionHeaderModel ->
                    oldItem.routeId == newItem.routeId
                oldItem is RouteDirectionItemModel && newItem is RouteDirectionItemModel ->
                    oldItem.routeId == newItem.routeId && oldItem.routeSeq == newItem.routeSeq
                oldItem is RouteDirectionEmptyModel && newItem is RouteDirectionEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: RouteDirectionListModel,
                newItem: RouteDirectionListModel
            ): Boolean = when {
                oldItem is RouteDirectionHeaderModel && newItem is RouteDirectionHeaderModel ->
                    oldItem == newItem
                oldItem is RouteDirectionItemModel && newItem is RouteDirectionItemModel ->
                    oldItem == newItem
                oldItem is RouteDirectionEmptyModel && newItem is RouteDirectionEmptyModel -> true
                else -> false
            }
        }
    }
}