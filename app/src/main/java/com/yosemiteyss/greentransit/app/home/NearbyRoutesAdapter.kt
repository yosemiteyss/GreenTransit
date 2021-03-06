//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.NearbyRoutesListItemBinding
import com.yosemiteyss.greentransit.app.databinding.RoutesEmptyItemBinding
import com.yosemiteyss.greentransit.app.home.NearbyRoutesListModel.NearbyRoutesEmptyModel
import com.yosemiteyss.greentransit.app.home.NearbyRoutesListModel.NearbyRoutesItemModel
import com.yosemiteyss.greentransit.app.home.NearbyRoutesViewHolder.NearbyRoutesEmptyViewHolder
import com.yosemiteyss.greentransit.app.home.NearbyRoutesViewHolder.NearbyRoutesItemViewHolder

class NearbyRoutesAdapter(
    private val onRouteClicked: (view: View, routeId: Long, routeCode: String) -> Unit
) : ListAdapter<NearbyRoutesListModel, NearbyRoutesViewHolder>(
    NearbyRoutesListModel.Diff
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyRoutesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.nearby_routes_list_item -> NearbyRoutesItemViewHolder(
                NearbyRoutesListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.routes_empty_item -> NearbyRoutesEmptyViewHolder(
                RoutesEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: NearbyRoutesViewHolder, position: Int) {
        when (holder) {
            is NearbyRoutesItemViewHolder -> bindNearbyRouteEtaItem(
                binding = holder.binding,
                itemModel = getItem(position) as NearbyRoutesItemModel
            )
            is NearbyRoutesEmptyViewHolder -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NearbyRoutesItemModel -> R.layout.nearby_routes_list_item
            is NearbyRoutesEmptyModel -> R.layout.routes_empty_item
        }
    }

    private fun bindNearbyRouteEtaItem(
        binding: NearbyRoutesListItemBinding,
        itemModel: NearbyRoutesItemModel
    ) = binding.run {
        routeCodeTextView.text = itemModel.routeCode
        routeDestTextView.text = root.context.getString(
            R.string.nearby_routes_dest,
            itemModel.routeDest
        )

        root.setOnClickListener {
            onRouteClicked(it, itemModel.routeId, itemModel.routeCode)
        }
    }
}

sealed class NearbyRoutesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class NearbyRoutesItemViewHolder(
        val binding: NearbyRoutesListItemBinding
    ) : NearbyRoutesViewHolder(binding.root)

    class NearbyRoutesEmptyViewHolder(
        binding: RoutesEmptyItemBinding
    ) : NearbyRoutesViewHolder(binding.root)
}

sealed class NearbyRoutesListModel {
    data class NearbyRoutesItemModel(
        val routeId: Long,
        val routeCode: String,
        val routeDest: String
    ) : NearbyRoutesListModel()

    object NearbyRoutesEmptyModel : NearbyRoutesListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<NearbyRoutesListModel>() {
            override fun areItemsTheSame(
                oldItem: NearbyRoutesListModel,
                newItem: NearbyRoutesListModel
            ): Boolean = when {
                oldItem is NearbyRoutesItemModel && newItem is NearbyRoutesItemModel ->
                    oldItem.routeId == newItem.routeId
                oldItem is NearbyRoutesEmptyModel && newItem is NearbyRoutesEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: NearbyRoutesListModel,
                newItem: NearbyRoutesListModel
            ): Boolean = when {
                oldItem is NearbyRoutesItemModel && newItem is NearbyRoutesItemModel ->
                    oldItem == newItem
                oldItem is NearbyRoutesEmptyModel && newItem is NearbyRoutesEmptyModel -> true
                else -> false
            }
        }
    }
}