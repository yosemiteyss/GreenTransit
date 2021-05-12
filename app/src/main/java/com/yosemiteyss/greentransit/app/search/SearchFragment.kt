package com.yosemiteyss.greentransit.app.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.*
import com.yosemiteyss.greentransit.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by kevin on 12/5/2021
 */

@AndroidEntryPoint
class SearchFragment : DialogFragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setStyle(STYLE_NORMAL, R.style.ThemeOverlay_GreenTransit_Dialog_Fullscreen_DayNight_Search)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutFullscreen(aboveNavBar = true)

        with(binding) {
            clearTextButton.applySystemWindowInsetsMargin(applyTop = true)
            loadingProgressBar.setVisibilityAfterHide(View.INVISIBLE)
            searchEditText.applySystemWindowInsetsMargin(applyTop = true)
            searchEditText.requestFocus()
        }

        // Clear text
        binding.clearTextButton.setOnClickListener {
            if (binding.searchEditText.text.isEmpty()) {
                findNavController(R.id.searchFragment)?.navigateUp()
            } else {
                binding.searchEditText.text.clear()
            }
        }

        // Dismiss when clicking scrim region
        viewLifecycleOwner.lifecycleScope.launch {
            binding.searchRecyclerView.touchOutsideItemsFlow().collect {
                findNavController(R.id.searchFragment)?.navigateUp()
            }
        }
    }
}