package com.yosemiteyss.greentransit.app.news

import android.os.Bundle
import android.os.Parcelable
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.databinding.FragmentNewsDetailBinding
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsMargin
import com.yosemiteyss.greentransit.app.utils.applySystemWindowInsetsPadding
import com.yosemiteyss.greentransit.app.utils.getColorCompat
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

        ViewCompat.setTranslationZ(requireView(),
            resources.getDimensionPixelSize(R.dimen.elevation_large).toFloat())

        binding.newsContentLayout.applySystemWindowInsetsMargin(applyBottom = true)
        binding.appBarLayout.applySystemWindowInsetsPadding(applyTop = true)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupNewsDetailLayout()
    }

    private fun setupNewsDetailLayout() {
        val trafficNews = navArgs.property.trafficNews

        val statusColor = when (trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> requireContext().getColorCompat(R.color.news_status_red)
            TrafficNewsStatus.UPDATED -> requireContext().getColorCompat(R.color.news_status_green)
        }

        val statusTitle = when (trafficNews.currentStatus) {
            TrafficNewsStatus.NEW -> getString(R.string.news_status_new)
            TrafficNewsStatus.UPDATED -> getString(R.string.news_status_updated)
        }.uppercase()

        binding.currentStatusTextView.text = statusTitle
        binding.currentStatusTextView.setTextColor(statusColor)

        binding.dateTextView.text = DateUtils.getRelativeTimeSpanString(
            trafficNews.referenceDate.time,
            Date().time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()

        binding.detailTextView.text = trafficNews.engShort
    }
}

@Parcelize
data class NewsDetailProperty(
    val trafficNews: @RawValue TrafficNews
) : Parcelable