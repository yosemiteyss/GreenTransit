//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsPadding
import com.yosemiteyss.greentransit.app.utils.showIf
import com.yosemiteyss.greentransit.app.utils.showShortToast
import com.yosemiteyss.greentransit.app.utils.viewBinding
import com.yosemiteyss.greentransit.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding: FragmentNewsBinding by viewBinding(FragmentNewsBinding::bind)
    val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.applySystemWindowInsetsPadding(applyTop = true)
        binding.loadingProgressBar.setVisibilityAfterHide(View.GONE)

        // Setup news recycler view
        val newsAdapter = NewsAdapter()

        binding.newsRecyclerView.run {
            adapter = newsAdapter
            setHasFixedSize(true)
        }

        viewModel.newsUiState.observe(viewLifecycleOwner) { uiState ->
            binding.loadingProgressBar.showIf(uiState is NewsUiState.Loading)

            if (uiState is NewsUiState.Success) {
                newsAdapter.submitList(uiState.data)
            } else if (uiState is NewsUiState.Error) {
                newsAdapter.submitList(uiState.data)
                showShortToast(uiState.message)
            }
        }
    }
}