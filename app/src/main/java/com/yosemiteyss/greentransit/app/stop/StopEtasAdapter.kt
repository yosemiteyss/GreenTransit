package com.yosemiteyss.greentransit.app.stop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.stop.StopEtasListModel.StopEtasEmptyModel
import com.yosemiteyss.greentransit.app.stop.StopEtasListModel.StopEtasShiftModel
import com.yosemiteyss.greentransit.app.stop.StopEtasViewHolder.StopEtasEmptyViewHolder
import com.yosemiteyss.greentransit.app.stop.StopEtasViewHolder.StopEtasShiftViewHolder
import com.yosemiteyss.greentransit.databinding.RoutesEmptyItemBinding
import com.yosemiteyss.greentransit.databinding.StopEtaListItemBinding
import com.yosemiteyss.greentransit.domain.models.StopEtaResult
import com.yosemiteyss.greentransit.domain.models.getEtaTimeString

/**
 * Created by kevin on 17/5/2021
 */

private const val MAX_DISPLAY_MINUTES = 59

class StopEtasAdapter : ListAdapter<StopEtasListModel, StopEtasViewHolder>(StopEtasListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopEtasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.stop_eta_list_item -> StopEtasShiftViewHolder(
                StopEtaListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.routes_empty_item -> StopEtasEmptyViewHolder(
                RoutesEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: StopEtasViewHolder, position: Int) {
        if (holder is StopEtasShiftViewHolder) {
            bindStopEtasShift(
                binding = holder.binding,
                etasShiftModel = getItem(position) as StopEtasShiftModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is StopEtasShiftModel -> R.layout.stop_eta_list_item
            is StopEtasEmptyModel -> R.layout.routes_empty_item
        }
    }

    private fun bindStopEtasShift(
        binding: StopEtaListItemBinding,
        etasShiftModel: StopEtasShiftModel
    ) = binding.run {
        val blinkAnim = AnimationUtils.loadAnimation(root.context, R.anim.blinking)
        loadingIconImageView.startAnimation(blinkAnim)

        routeCodeTextView.text = etasShiftModel.stopEtaResult.remarks?.let {
            root.context.getString(
                R.string.stop_eta_shift_code,
                etasShiftModel.stopEtaResult.routeCode.code,
                it
            )
        } ?: etasShiftModel.stopEtaResult.routeCode.code

        routeDestTextView.text = root.context.getString(
            R.string.stop_eta_shift_dest,
            etasShiftModel.stopEtaResult.dest
        )

        if (etasShiftModel.stopEtaResult.etaMin <= MAX_DISPLAY_MINUTES) {
            shiftEtaMinTextView.text = etasShiftModel.stopEtaResult.etaMin.toString()
            shiftEtaMinSuffixTextView.isVisible = true

        } else {
            shiftEtaMinTextView.text = etasShiftModel.stopEtaResult.getEtaTimeString()
            shiftEtaMinSuffixTextView.isVisible = false
        }
    }
}

sealed class StopEtasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class StopEtasShiftViewHolder(
        val binding: StopEtaListItemBinding
    ) : StopEtasViewHolder(binding.root)

    class StopEtasEmptyViewHolder(
        val binding: RoutesEmptyItemBinding
    ) : StopEtasViewHolder(binding.root)
}

sealed class StopEtasListModel {
    data class StopEtasShiftModel(
        val stopEtaResult: StopEtaResult
    ) : StopEtasListModel()

    object StopEtasEmptyModel : StopEtasListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<StopEtasListModel>() {
            override fun areItemsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.stopEtaResult.routeId == newItem.stopEtaResult.routeId &&
                    oldItem.stopEtaResult.routeSeq == newItem.stopEtaResult.routeSeq
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.stopEtaResult == newItem.stopEtaResult &&
                        oldItem.stopEtaResult == newItem.stopEtaResult
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }
        }
    }
}