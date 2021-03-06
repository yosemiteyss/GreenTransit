//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.news

import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.NewsEmptyItemBinding
import com.yosemiteyss.greentransit.app.databinding.NewsHeaderItemBinding
import com.yosemiteyss.greentransit.app.databinding.NewsListItemBinding
import com.yosemiteyss.greentransit.app.news.TrafficNewsListModel.*
import com.yosemiteyss.greentransit.app.utils.getColorCompat
import com.yosemiteyss.greentransit.app.utils.getDrawableOrNull
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import java.util.*

class NewsAdapter(
    private val onNewsClicked: (trafficNews: TrafficNews) -> Unit
) : ListAdapter<TrafficNewsListModel, TrafficNewsViewHolder>(TrafficNewsListModelDiff) {
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
        // News status
        val statusColor = when (trafficNewsListModel.trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> root.context.getColorCompat(R.color.news_status_red)
            TrafficNewsStatus.UPDATED -> root.context.getColorCompat(R.color.news_status_green)
        }

        val statusDrawable = when (trafficNewsListModel.trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> root.context.getDrawableOrNull(R.drawable.ic_error)
            TrafficNewsStatus.UPDATED -> root.context.getDrawableOrNull(R.drawable.ic_update)
        }

        val statusTitle =  when (trafficNewsListModel.trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> root.context.getString(R.string.news_status_new)
            TrafficNewsStatus.UPDATED -> root.context.getString(R.string.news_status_updated)
        }.uppercase()

        currentStatusTextView.text = statusTitle
        currentStatusTextView.setTextColor(statusColor)
        currentStatusTextView.setCompoundDrawablesWithIntrinsicBounds(
            statusDrawable, null, null, null
        )
        TextViewCompat.setCompoundDrawableTintList(
            currentStatusTextView,
            ColorStateList.valueOf(statusColor)
        )

        // News date
        dateTextView.text = DateUtils.getRelativeTimeSpanString(
            trafficNewsListModel.trafficNews.referenceDate.time,
            Date().time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()

        // News content
        contentTextView.text = trafficNewsListModel.trafficNews.engShort

        newsCardView.setOnClickListener {
            onNewsClicked(trafficNewsListModel.trafficNews)
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