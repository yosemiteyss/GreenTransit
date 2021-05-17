package com.yosemiteyss.greentransit.app.stop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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
import com.yosemiteyss.greentransit.domain.models.StopEtaShiftWithCode


/**
 * Created by kevin on 17/5/2021asasz
 */

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

        routeCodeTextView.text = etasShiftModel.stopEtaShiftWithCode.routeCode.code
        // TODO: dest/stop name
        shiftEtaMinTextView.text = etasShiftModel.stopEtaShiftWithCode.etaShift.etaMin.toString()
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
        val stopEtaShiftWithCode: StopEtaShiftWithCode
    ) : StopEtasListModel()

    object StopEtasEmptyModel : StopEtasListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<StopEtasListModel>() {
            override fun areItemsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.stopEtaShiftWithCode.etaShift.routeId ==
                        newItem.stopEtaShiftWithCode.etaShift.routeId &&
                    oldItem.stopEtaShiftWithCode.etaShift.etaSeq ==
                        newItem.stopEtaShiftWithCode.etaShift.etaSeq
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.stopEtaShiftWithCode == newItem.stopEtaShiftWithCode
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }
        }
    }
}