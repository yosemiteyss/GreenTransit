package com.yosemiteyss.greentransit.app.news

import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.news.TrafficNewsListModel.*
import com.yosemiteyss.greentransit.app.utils.getColorCompat
import com.yosemiteyss.greentransit.databinding.NewsEmptyItemBinding
import com.yosemiteyss.greentransit.databinding.NewsHeaderItemBinding
import com.yosemiteyss.greentransit.databinding.NewsListItemBinding
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import java.util.*

/**
 * Created by kevin on 14/5/2021
 */

private const val NEWS_COLLAPSED_LINES = 3
private const val NEWS_MAX_LINES = Int.MAX_VALUE

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
        if (holder is TrafficNewsViewHolder.TrafficNewsItemViewHolder) {
            bindNewsItem(
                binding = holder.binding,
                trafficNewsListModel = getItem(position) as TrafficNewsItem
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TrafficNewsHeader -> R.layout.news_header_item
            is TrafficNewsItem -> R.layout.news_list_item
            is TrafficNewsEmptyItem -> R.layout.news_empty_item
        }
    }

    private fun bindNewsItem(
        binding: NewsListItemBinding,
        trafficNewsListModel: TrafficNewsItem
    ) = binding.run {
        currentStatusTextView.text = trafficNewsListModel.trafficNews.currentStatus.toString()

        val statusColor = when (trafficNewsListModel.trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> root.context.getColorCompat(R.color.news_status_red)
            TrafficNewsStatus.UPDATED -> root.context.getColorCompat(R.color.news_status_green)
        }

        currentStatusTextView.backgroundTintList = ColorStateList.valueOf(statusColor)

        val relativeTime = DateUtils.getRelativeTimeSpanString(
            trafficNewsListModel.trafficNews.referenceDate.time,
            Date().time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()

        dateTextView.text = root.context.getString(
            R.string.news_last_updated,
            relativeTime
        )

        with(contentTextView) {
            text = trafficNewsListModel.trafficNews.engShort
            maxLines = NEWS_COLLAPSED_LINES
        }

        // Collapse or expand
        root.setOnClickListener {
            contentTextView.maxLines = if (contentTextView.maxLines == NEWS_COLLAPSED_LINES)
                NEWS_MAX_LINES else NEWS_COLLAPSED_LINES
        }
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

    data class TrafficNewsItem(val trafficNews: TrafficNews) : TrafficNewsListModel()

    object TrafficNewsEmptyItem: TrafficNewsListModel()
}

object TrafficNewsListModelDiff : DiffUtil.ItemCallback<TrafficNewsListModel>() {
    override fun areItemsTheSame(
        oldItem: TrafficNewsListModel,
        newItem: TrafficNewsListModel
    ) : Boolean {
        return when {
            oldItem is TrafficNewsItem && newItem is TrafficNewsItem ->
                oldItem.trafficNews.msgId == newItem.trafficNews.msgId
            oldItem is TrafficNewsHeader && newItem is TrafficNewsHeader ->
                true
            oldItem is TrafficNewsEmptyItem && newItem is TrafficNewsEmptyItem ->
                true
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: TrafficNewsListModel,
        newItem: TrafficNewsListModel
    ): Boolean {
        return when {
            oldItem is TrafficNewsItem && newItem is TrafficNewsItem ->
                oldItem.trafficNews == newItem.trafficNews
            oldItem is TrafficNewsHeader && newItem is TrafficNewsHeader ->
                true
            oldItem is TrafficNewsEmptyItem && newItem is TrafficNewsEmptyItem ->
                true
            else -> false
        }
    }
}