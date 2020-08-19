package com.poovam.giphygallery.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poovam.giphygallery.R
import com.poovam.giphygallery.favourites.FavouritesFragment
import com.poovam.giphygallery.trending.TrendingFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter = MainViewPagerAdapter()
    }

    private inner class MainViewPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            if (position == 0) TrendingFragment() else FavouritesFragment()
    }
}
