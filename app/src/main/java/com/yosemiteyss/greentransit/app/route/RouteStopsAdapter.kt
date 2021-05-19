package com.yosemiteyss.greentransit.app.route

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopEmptyModel
import com.yosemiteyss.greentransit.app.route.RouteStopsListModel.RouteStopItemModel
import com.yosemiteyss.greentransit.app.route.RouteStopsViewHolder.RouteStopsEmptyViewHolder
import com.yosemiteyss.greentransit.app.route.RouteStopsViewHolder.RouteStopsItemViewHolder
import com.yosemiteyss.greentransit.app.utils.drawableFitVertical
import com.yosemiteyss.greentransit.app.utils.format
import com.yosemiteyss.greentransit.app.utils.getDrawableOrNull
import com.yosemiteyss.greentransit.app.utils.themeColor
import com.yosemiteyss.greentransit.databinding.RouteStopListItemBinding
import com.yosemiteyss.greentransit.databinding.StopsEmptyItemBinding
import java.util.*

/**
 * Created by kevin on 18/5/2021
 */

private const val MAX_DISPLAY_MINUTES = 59

class RouteStopsAdapter(
    private val onStopClicked: (stopId: Long) -> Unit
) : RecyclerView.Adapter<RouteStopsViewHolder>() {

    private val differ = AsyncListDiffer(this, RouteStopsListModel.Diff)

    var routeStopsListModels: List<RouteStopsListModel> = emptyList()
        set(value) {
            field = value
            selectedPosition = RecyclerView.NO_POSITION
            differ.submitList(value)
        }

    private var selectedPosition: Int = RecyclerView.NO_POSITION
        set(value) {
            if (value != field) {
                // Uncheck previous item
                if (field != RecyclerView.NO_POSITION) {
                    val previousItem = differ.currentList[field]
                    if (previousItem is RouteStopItemModel &&
                        previousItem.isSelected
                    ) {
                        previousItem.isSelected = false
                        notifyItemChanged(field)
                    }
                }

                // Check new item
                if (value != RecyclerView.NO_POSITION) {
                    val selectedItem = differ.currentList[value]
                    if (selectedItem is RouteStopItemModel &&
                        !selectedItem.isSelected
                    ) {
                        selectedItem.isSelected = true
                        onStopClicked(selectedItem.stopId)
                        notifyItemChanged(value)
                    }
                }

                field = value
            }
        }

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
                stopItemModel = differ.currentList[position] as RouteStopItemModel,
                position = position
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is RouteStopItemModel -> R.layout.route_stop_list_item
            is RouteStopEmptyModel -> R.layout.stops_empty_item
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private fun bindRouteStopItem(
        binding: RouteStopListItemBinding,
        stopItemModel: RouteStopItemModel,
        position: Int
    ) = binding.run {
        stopNameTextView.text = stopItemModel.stopName

        with(stopSeqTextView) {
            text = stopItemModel.stopSeq.toString()

            if (stopItemModel.isSelected) {
                setTextColor(context.themeColor(R.attr.colorOnPrimary))
                background = context.getDrawableOrNull(R.drawable.shape_circle)
                backgroundTintList = ColorStateList.valueOf(
                    context.themeColor(R.attr.colorPrimary)
                )
            } else {
                setTextColor(context.themeColor(R.attr.colorPrimary))
                background = null
            }
        }

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

        root.setOnClickListener {
            onStopClicked(stopItemModel.stopId)
            selectedPosition = position
        }
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
        var isSelected: Boolean = false
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