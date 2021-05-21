//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk


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
import com.yosemiteyss.greentransit.app.utils.format
import com.yosemiteyss.greentransit.databinding.RoutesEmptyItemBinding
import com.yosemiteyss.greentransit.databinding.StopEtaListItemBinding
import com.yosemiteyss.greentransit.domain.models.StopRouteShiftEtaResult

private const val MAX_DISPLAY_MINUTES = 59

class StopEtasAdapter(
    private val onRouteClicked: (routeId: Long, routeCode: String) -> Unit
) : ListAdapter<StopEtasListModel, StopEtasViewHolder>(StopEtasListModel.Diff) {

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

        routeCodeTextView.text = etasShiftModel.routeShiftEtaResult.remarks?.let {
            root.context.getString(
                R.string.stop_eta_shift_code,
                etasShiftModel.routeShiftEtaResult.routeCode.code,
                it
            )
        } ?: etasShiftModel.routeShiftEtaResult.routeCode.code

        routeDestTextView.text = root.context.getString(
            R.string.stop_eta_shift_dest,
            etasShiftModel.routeShiftEtaResult.dest
        )

        if (etasShiftModel.routeShiftEtaResult.etaMin <= MAX_DISPLAY_MINUTES) {
            shiftEtaMinTextView.text = etasShiftModel.routeShiftEtaResult.etaMin.toString()
            shiftEtaMinSuffixTextView.isVisible = true

        } else {
            shiftEtaMinTextView.text = etasShiftModel.routeShiftEtaResult.etaDate
                .format("HH:mm")
            shiftEtaMinSuffixTextView.isVisible = false
        }

        root.setOnClickListener {
            onRouteClicked(
                etasShiftModel.routeShiftEtaResult.routeId,
                etasShiftModel.routeShiftEtaResult.routeCode.code
            )
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
        val routeShiftEtaResult: StopRouteShiftEtaResult
    ) : StopEtasListModel()

    object StopEtasEmptyModel : StopEtasListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<StopEtasListModel>() {
            override fun areItemsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.routeShiftEtaResult.routeId == newItem.routeShiftEtaResult.routeId &&
                    oldItem.routeShiftEtaResult.routeSeq == newItem.routeShiftEtaResult.routeSeq
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: StopEtasListModel,
                newItem: StopEtasListModel
            ): Boolean = when {
                oldItem is StopEtasShiftModel && newItem is StopEtasShiftModel ->
                    oldItem.routeShiftEtaResult == newItem.routeShiftEtaResult &&
                        oldItem.routeShiftEtaResult == newItem.routeShiftEtaResult
                oldItem is StopEtasEmptyModel && newItem is StopEtasEmptyModel -> true
                else -> false
            }
        }
    }
}