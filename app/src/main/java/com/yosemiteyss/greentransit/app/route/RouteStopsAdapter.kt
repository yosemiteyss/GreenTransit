package com.yosemiteyss.greentransit.app.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopEmptyModel
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopItemModel
import com.yosemiteyss.greentransit.app.route.RouteStopsViewHolder.RouteStopsEmptyViewHolder
import com.yosemiteyss.greentransit.app.route.RouteStopsViewHolder.RouteStopsItemViewHolder
import com.yosemiteyss.greentransit.app.utils.drawableFitVertical
import com.yosemiteyss.greentransit.app.utils.format
import com.yosemiteyss.greentransit.app.utils.getDrawableOrNull
import com.yosemiteyss.greentransit.databinding.RouteStopListItemBinding
import com.yosemiteyss.greentransit.databinding.StopsEmptyItemBinding
import java.util.*

/**
 * Created by kevin on 18/5/2021
 */

private const val MAX_DISPLAY_MINUTES = 59

class RouteStopsAdapter(

) : ListAdapter<RouteStopsListModel, RouteStopsViewHolder>(RouteStopsListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteStopsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.route_stop_list_item -> RouteStopsItemViewHolder(
                RouteStopListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.stops_empty_item -> RouteStopsEmptyViewHolder(
                StopsEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RouteStopsViewHolder, position: Int) {
        if (holder is RouteStopsItemViewHolder) {
            bindRouteStopItem(
                binding = holder.binding,
                stopItemModel = getItem(position) as RouteStopItemModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RouteStopItemModel -> R.layout.route_stop_list_item
            is RouteStopEmptyModel -> R.layout.stops_empty_item
        }
    }

    private fun bindRouteStopItem(
        binding: RouteStopListItemBinding,
        stopItemModel: RouteStopItemModel
    ) = binding.run {
        stopNameTextView.text = stopItemModel.stopName
        stopSeqTextView.text = stopItemModel.stopSeq.toString()

        with(etaRemarksDescriptionTextView) {
            isVisible = !stopItemModel.etaDescription.isNullOrBlank() ||
                !stopItemModel.etaRemarks.isNullOrBlank()

            text = stopItemModel.etaDescription ?: stopItemModel.etaRemarks

            if (!stopItemModel.etaDescription.isNullOrBlank()) {
                setCompoundDrawablesRelative(
                    root.context.getDrawableOrNull(R.drawable.ic_info),
                    null, null, null
                )

                drawableFitVertical()
            }
        }

        stopItemModel.etaMin?.let {
            etaMinTextView.text = if (it <= MAX_DISPLAY_MINUTES) {
                it.toString()
            } else {
                stopItemModel.etaDate?.format("HH:mm")
            }
        }

        etaMinSuffixTextView.isVisible = stopItemModel.etaMin != null &&
            stopItemModel.etaMin <= MAX_DISPLAY_MINUTES
    }
}

sealed class RouteStopsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class RouteStopsItemViewHolder(
        val binding: RouteStopListItemBinding
    ) : RouteStopsViewHolder(binding.root)

    class RouteStopsEmptyViewHolder(
        val binding: StopsEmptyItemBinding
    ) : RouteStopsViewHolder(binding.root)
}

sealed class RouteStopsListModel {
    data class RouteStopItemModel(
        val stopId: Long,
        val stopSeq: Int,
        val stopName: String,
        val etaEnabled: Boolean,
        val etaDescription: String?,
        val etaMin: Int?,
        val etaDate: Date?,
        val etaRemarks: String?,
        val isCurrent: Boolean = false
    ) : RouteStopsListModel()

    object RouteStopEmptyModel : RouteStopsListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<RouteStopsListModel>() {
            override fun areItemsTheSame(
                oldItem: RouteStopsListModel,
                newItem: RouteStopsListModel
            ): Boolean = when {
                oldItem is RouteStopItemModel && newItem is RouteStopItemModel ->
                    oldItem.stopId == newItem.stopId
                oldItem is RouteStopEmptyModel && newItem is RouteStopEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: RouteStopsListModel,
                newItem: RouteStopsListModel
            ): Boolean= when {
                oldItem is RouteStopItemModel && newItem is RouteStopItemModel ->
                    oldItem == newItem
                oldItem is RouteStopEmptyModel && newItem is RouteStopEmptyModel -> true
                else -> false
            }
        }
    }
}