package com.yosemiteyss.greentransit.app.news

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.filters.MediumTest
import com.yosemiteyss.greentransit.app.launchFragmentInHiltContainer
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
class NewsFragmentTest {

    @BindValue
    val mockNewsViewModel = mockk<NewsViewModel>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testLaunchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<NewsFragment> {

        }
        /*
        onView(withId(R.id.news_recycler_view))
            .perform(
                RecyclerViewActions
                .actionOnItemAtPosition<TrafficNewsViewHolder>(1, clickNewsItemWithId(R.id.news_list_item_layout)
                ))*/
    }
}

fun clickNewsItemWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click News Item with a specific ID"
        }

        override fun perform(uiController: UiController?, view: View) {
            val v = view.findViewById<View>(id)
            v.performClick()
        }

    }
}