package com.yosemiteyss.greentransit.app.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsPadding
import com.yosemiteyss.greentransit.app.utils.viewBinding
import com.yosemiteyss.greentransit.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by kevin on 12/5/2021
 */

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding: FragmentNewsBinding by viewBinding(FragmentNewsBinding::bind)
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applySystemWindowInsetsPadding(applyTop = true)

        // Setup news recycler view
        val newsAdapter = NewsAdapter()

        binding.newsRecyclerView.run {
            adapter = newsAdapter
            setHasFixedSize(true)
        }

        viewModel.trafficNews.observe(viewLifecycleOwner) {
            newsAdapter.submitList(it)
        }
    }
}