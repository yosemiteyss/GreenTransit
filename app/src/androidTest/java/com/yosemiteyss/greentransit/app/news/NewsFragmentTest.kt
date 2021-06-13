//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.news

import android.widget.TextView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.yosemiteyss.greentransit.app.R
import com.yosemiteyss.greentransit.app.launchFragmentInHiltContainer
import com.yosemiteyss.greentransit.data.di.TrafficNewsModule
import com.yosemiteyss.greentransit.data.mappers.TrafficNewsMapper
import com.yosemiteyss.greentransit.domain.repositories.TrafficNewsRepository
import com.yosemiteyss.greentransit.testshared.repositories.FakeTrafficNewsRepositoryImpl
import com.yosemiteyss.greentransit.testshared.utils.TestCoroutineRule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@SmallTest
@HiltAndroidTest
@UninstallModules(TrafficNewsModule::class)
class NewsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var coroutineRule = TestCoroutineRule()

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Module
    @InstallIn(SingletonComponent::class)
    object TrafficNewsTestModule {
        @Singleton
        @Provides
        fun provideTrafficNewsRepository(): TrafficNewsRepository = FakeTrafficNewsRepositoryImpl()

        @Singleton
        @Provides
        fun provideTrafficNewsMapper(): TrafficNewsMapper = TrafficNewsMapper()
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_news_header() {
        launchFragmentInHiltContainer<NewsFragment>()

        assertDisplayed(R.id.news_recycler_view)

        assertDisplayedAtPosition(
            R.id.news_recycler_view, 0,
            R.id.news_header_title_text_view, context.getString(R.string.news_header_title)
        )

        assertDisplayedAtPosition(
            R.id.news_recycler_view, 0,
            R.id.news_header_subtitle_text_view, context.getString(R.string.news_header_subtitle)
        )
    }

    @Test
    fun test_news_card_max_lines() {
        launchFragmentInHiltContainer<NewsFragment>()

        assertDisplayed(R.id.news_recycler_view)

        assertCustomAssertionAtPosition(
            R.id.news_recycler_view, 1,
            R.id.content_text_view, hasMaxLines(3)
        )
    }
}

fun hasMaxLines(count: Int) : ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) throw noViewFoundException
        if (view !is TextView) throw IllegalStateException("Not a TextView.")
        assertThat("Textview max lines", view.maxLines, CoreMatchers.equalTo(count))
    }
}