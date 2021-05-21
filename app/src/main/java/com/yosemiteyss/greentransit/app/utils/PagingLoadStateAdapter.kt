//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemiteyss.greentransit.databinding.PagingLoadStateItemBinding

class PagingLoadStateAdapter(
    private val retryAction: () -> Unit
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PagingLoadStateViewHolder(
            PagingLoadStateItemBinding.inflate(inflater, parent, false),
            retryAction
        )
    }
}

class PagingLoadStateViewHolder(
    private val binding: PagingLoadStateItemBinding,
    private val retryAction: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) = binding.run {
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState !is LoadState.Loading
        retryButton.setOnClickListener { retryAction() }
    }
}