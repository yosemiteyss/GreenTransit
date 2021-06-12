package com.yosemiteyss.greentransit.app.news

import android.os.Bundle
import android.os.Parcelable
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentNewsDetailBinding
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsMargin
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsPadding
import com.yosemiteyss.greentransit.app.utils.viewBinding
import com.yosemiteyss.greentransit.domain.models.TrafficNews
import com.yosemiteyss.greentransit.domain.models.TrafficNewsStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

/**
 * Created by kevin on 11/6/2021
 */

@AndroidEntryPoint
class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val binding: FragmentNewsDetailBinding by viewBinding(FragmentNewsDetailBinding::bind)
    private val navArgs: NewsDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            appBarLayout.applySystemWindowInsetsPadding(applyTop = true)
            newsContentLayout.applySystemWindowInsetsMargin(applyBottom = true)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupNewsDetailLayout()
    }

    private fun setupNewsDetailLayout() {
        val trafficNews = navArgs.property.trafficNews

//        val statusColor = when (trafficNews.currentStatus) {
//            TrafficNewsStatus.NEW -> requireContext().getColorCompat(R.color.news_status_red)
//            TrafficNewsStatus.UPDATED -> requireContext().getColorCompat(R.color.news_status_green)
//        }

        val statusTitle = when (trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> getString(R.string.news_detail_status_new_title)
            TrafficNewsStatus.UPDATED -> getString(R.string.news_detail_status_updated_title)
        }

        binding.collapsingToolbarLayout.title = statusTitle

        binding.dateTextView.text = getString(
            R.string.news_detail_date,
            DateUtils.getRelativeTimeSpanString(
                trafficNews.referenceDate.time,
                Date().time,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
        )

        binding.detailTextView.text = trafficNews.engShort
    }
}

@Parcelize
data class NewsDetailProperty(
    val trafficNews: @RawValue TrafficNews
) : Parcelable