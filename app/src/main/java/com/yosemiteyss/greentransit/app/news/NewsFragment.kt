//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentNewsBinding
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsMargin
import com.yosemiteyss.greentransit.app.utils.showIf
import com.yosemiteyss.greentransit.app.utils.showShortToast
import com.yosemiteyss.greentransit.app.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding: FragmentNewsBinding by viewBinding(FragmentNewsBinding::bind)
    val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.applySystemWindowInsetsMargin(applyTop = true)
        binding.loadingProgressBar.setVisibilityAfterHide(View.GONE)

        // Setup news recycler view
        val newsAdapter = NewsAdapter {
            findNavController().navigate(
                NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(
                    NewsDetailProperty(it)
                )
            )
        }

        binding.newsRecyclerView.run {
            adapter = newsAdapter
            setHasFixedSize(true)
        }

        viewModel.newsUiState.observe(viewLifecycleOwner) { uiState ->
            binding.loadingProgressBar.showIf(
            uiState is NewsUiState.Loading &&
                !uiState.isSwipeRefresh
            )

            if (uiState is NewsUiState.Success) {
                newsAdapter.submitList(uiState.data)
                binding.swipeRefreshLayout.isRefreshing = false
            } else if (uiState is NewsUiState.Error) {
                newsAdapter.submitList(uiState.data)
                showShortToast(uiState.message)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        // Swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadTrafficNews(isSwipeRefresh = true)
        }
    }
}