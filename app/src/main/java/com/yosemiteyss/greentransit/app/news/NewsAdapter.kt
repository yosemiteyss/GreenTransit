package com.yosemiteyss.greentransit.app.news

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.databinding.NewsEmptyItemBinding
import com.yosemiteyss.greentransit.databinding.NewsHeaderItemBinding
import com.yosemiteyss.greentransit.databinding.NewsListItemBinding
import com.yosemiteyss.greentransit.domain.models.TrafficNews

/**
 * Created by kevin on 14/5/2021
 */

class NewsAdapter : ListAdapter<TrafficNewsListModel, TrafficNewsViewHolder>(TrafficNewsListModelDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrafficNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.news_header_item -> TrafficNewsViewHolder.TrafficNewsHeaderViewHolder(
                NewsHeaderItemBinding.inflate(inflater, parent, false)
            )
            R.layout.news_list_item -> TrafficNewsViewHolder.TrafficNewsItemViewHolder(
                NewsListItemBinding.inflate(inflater, parent, false)
            )
            R.layout.news_empty_item -> TrafficNewsViewHolder.TrafficNewsItemEmptyViewHolder(
                NewsEmptyItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: TrafficNewsViewHolder, position: Int) {
        Log.d("tag", (holder is TrafficNewsViewHolder.TrafficNewsItemViewHolder).toString())
        Log.d("tag", holder.toString())
        if (holder is TrafficNewsViewHolder.TrafficNewsItemViewHolder) {
            bindNewsItem(binding = holder.binding, trafficNewsListModel = getItem(position) as TrafficNewsListModel.TrafficNewsItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TrafficNewsListModel.TrafficNewsHeader -> R.layout.news_header_item
            is TrafficNewsListModel.TrafficNewsItem -> R.layout.news_list_item
            is TrafficNewsListModel.TrafficNewsEmptyItem -> R.layout.news_empty_item
        }
    }

    private fun bindNewsItem(
        binding: NewsListItemBinding,
        trafficNewsListModel: TrafficNewsListModel.TrafficNewsItem
    ) = binding.run {
        val context = root.context
        Log.d("tag", "binding news items")
        Log.d("tag", trafficNewsListModel.trafficNews.msgId)
        // TODO: Waiting for Billy's completion
        msgID.text = trafficNewsListModel.trafficNews.msgId
        currentStatus.text = trafficNewsListModel.trafficNews.currentStatus.toString()
        engShort.text = trafficNewsListModel.trafficNews.engShort
    }
}

sealed class TrafficNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class TrafficNewsHeaderViewHolder(
        val binding: NewsHeaderItemBinding
    ) : TrafficNewsViewHolder(binding.root)
    class TrafficNewsItemViewHolder(
        val binding: NewsListItemBinding
    ) : TrafficNewsViewHolder(binding.root)
    class TrafficNewsItemEmptyViewHolder(
        val binding: NewsEmptyItemBinding
    ) : TrafficNewsViewHolder(binding.root)
}

sealed class TrafficNewsListModel {
    object TrafficNewsHeader : TrafficNewsListModel()
    data class TrafficNewsItem(
        val trafficNews: TrafficNews
    ) : TrafficNewsListModel()
    object TrafficNewsEmptyItem: TrafficNewsListModel()
}

object TrafficNewsListModelDiff : DiffUtil.ItemCallback<TrafficNewsListModel>() {
    override fun areItemsTheSame(
        oldItem: TrafficNewsListModel,
        newItem: TrafficNewsListModel
    ) : Boolean {
        return when {
            oldItem is TrafficNewsListModel.TrafficNewsItem && newItem is TrafficNewsListModel.TrafficNewsItem ->
                oldItem.trafficNews.msgId == newItem.trafficNews.msgId
            oldItem is TrafficNewsListModel.TrafficNewsHeader && newItem is TrafficNewsListModel.TrafficNewsHeader ->
                true
            oldItem is TrafficNewsListModel.TrafficNewsEmptyItem && newItem is TrafficNewsListModel.TrafficNewsEmptyItem ->
                true
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: TrafficNewsListModel,
        newItem: TrafficNewsListModel
    ): Boolean {
        return when {
            oldItem is TrafficNewsListModel.TrafficNewsItem && newItem is TrafficNewsListModel.TrafficNewsItem ->
                oldItem.trafficNews == newItem.trafficNews
            oldItem is TrafficNewsListModel.TrafficNewsHeader && newItem is TrafficNewsListModel.TrafficNewsHeader ->
                true
            oldItem is TrafficNewsListModel.TrafficNewsEmptyItem && newItem is TrafficNewsListModel.TrafficNewsEmptyItem ->
                true
            else -> false
        }
    }
}