package com.yosemiteyss.greentransit.app.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.search.SearchListModel.SearchResultListModel
import com.yosemiteyss.greentransit.app.search.SearchViewHolder.SearchResultViewHolder
import com.yosemiteyss.greentransit.databinding.SearchResultItemBinding
import com.yosemiteyss.greentransit.domain.models.RouteCode

/**
 * Created by kevin on 16/5/2021
 */

class SearchAdapter(
    private val onResultClicked: (routeCode: RouteCode) -> Unit
) : ListAdapter<SearchListModel, SearchViewHolder>(SearchListModel.Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.search_result_item -> SearchResultViewHolder(
                SearchResultItemBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        when (holder) {
            is SearchResultViewHolder -> bindSearchResultItem(
                binding = holder.binding,
                resultListModel = getItem(position) as SearchResultListModel
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchResultListModel -> R.layout.search_result_item
        }
    }

    private fun bindSearchResultItem(
        binding: SearchResultItemBinding,
        resultListModel: SearchResultListModel
    ) = binding.run {
        routeCodeTextView.text = resultListModel.routeCode.code
        routeRegionTextView.text = resultListModel.routeCode.region.abbr
        root.setOnClickListener { onResultClicked(resultListModel.routeCode) }
    }
}

sealed class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class SearchResultViewHolder(
        val binding: SearchResultItemBinding
    ) : SearchViewHolder(binding.root)
}

sealed class SearchListModel {
    data class SearchResultListModel(
        val routeCode: RouteCode
    ) : SearchListModel()

    companion object {
        val Diff = object : DiffUtil.ItemCallback<SearchListModel>() {
            override fun areItemsTheSame(
                oldItem: SearchListModel,
                newItem: SearchListModel
            ): Boolean = when {
                oldItem is SearchResultListModel && newItem is SearchResultListModel ->
                    oldItem == newItem
                else -> false
            }

            override fun areContentsTheSame(
                oldItem: SearchListModel,
                newItem: SearchListModel
            ): Boolean = when {
                oldItem is SearchResultListModel && newItem is SearchResultListModel ->
                    oldItem == newItem
                else -> false
            }
        }
    }
}
