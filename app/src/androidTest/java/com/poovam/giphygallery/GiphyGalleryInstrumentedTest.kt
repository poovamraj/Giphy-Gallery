package com.poovam.giphygallery

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.poovam.giphygallery.common.view.GifItemViewHolder
import com.poovam.giphygallery.main.view.MainActivity
import com.poovam.giphygallery.trending.view.GifRecyclerAdapter
import com.poovam.giphygallery.trending.view.TrendingAndSearchFragment
import kotlinx.android.synthetic.main.favourites_fragment.*
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GiphyGalleryInstrumentedTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkLoadingIsShown() {
        onView(withId(R.id.swipeRefreshLayout)).perform(ViewActions.swipeDown()).check(
            matches(
                ViewMatchers.isEnabled()
            )
        )
    }

    @Test
    fun checkLoadingIsShownWhenSearched() {
        onView(withId(R.id.searchView)).perform(ViewActions.click())
            .perform(typeSearchViewText("Dab"))
        onView(withId(R.id.swipeRefreshLayout)).perform(ViewActions.swipeDown()).check(
            matches(
                ViewMatchers.isEnabled()
            )
        )
    }

    @Test
    fun checkWhetherPopupViewIsShownWhenGifIsClicked() {
        Thread.sleep(5000) //TODO using Idling here
        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GifItemViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.gifPopupView)).check(matches(isDisplayed()))
    }

    @Test
    fun checkWhetherFavouriteButtonViewChangesOnClicked_AndCheckFavouritesCountsChanges() {
        Thread.sleep(5000) //TODO using Idling here
        val trendingFragment =
            (rule.activity as MainActivity).supportFragmentManager.fragments[0] as TrendingAndSearchFragment

        val adapter = trendingFragment.recyclerView.adapter as GifRecyclerAdapter
        val isFirstItemFavourite =
            adapter.favourites.find { it.id == adapter.currentList?.get(0)?.id } != null
        val totalFavouritesBeforeClick = adapter.favourites.size

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GifItemViewHolder>(
                0,
                MyViewAction.clickChildViewWithId(R.id.favourite)
            )
        )
        Thread.sleep(5000) //TODO using Idling here
        val drawable = if(isFirstItemFavourite) R.drawable.ic_heart_off else R.drawable.ic_heart_on
        onView(withId(R.id.recyclerView)).check(matches(atPosition(0, hasDescendant(
            withDrawable(drawable)
        ))))

        val totalFavouritesSizeAfterClick = adapter.favourites.size

        if(isFirstItemFavourite) {
            assertThat(totalFavouritesSizeAfterClick, `is`(totalFavouritesBeforeClick - 1))
        } else {
            assertThat(totalFavouritesSizeAfterClick, `is`(totalFavouritesBeforeClick + 1))
        }
    }
}