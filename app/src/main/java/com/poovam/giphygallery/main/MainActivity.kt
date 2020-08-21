package com.poovam.giphygallery.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.poovam.giphygallery.R
import com.poovam.giphygallery.favourites.view.FavouritesFragment
import com.poovam.giphygallery.trending.view.TrendingAndSearchFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    /**
     * This model is used to maintain association between the fragment and its title
     * This way, mistake cannot be made with respect to providing one less title or fragment
     * Since fragment shouldn't have custom constructor this shouldn't be an issue
     * Also used Generic in a way that only Fragment can be provided
     */
    data class Page<T : Fragment>(val fragment: Class<T>, @StringRes val stringRes: Int)

    val pages = listOf(
        Page(TrendingAndSearchFragment::class.java, R.string.trending),
        Page(FavouritesFragment::class.java, R.string.favourites)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter = MainViewPagerAdapter()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(pages[position].stringRes)
        }.attach()
    }

    private inner class MainViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = pages.size

        override fun createFragment(position: Int): Fragment =
            pages[position].fragment.newInstance()
    }
}
