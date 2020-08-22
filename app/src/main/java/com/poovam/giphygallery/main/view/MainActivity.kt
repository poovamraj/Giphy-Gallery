package com.poovam.giphygallery.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.poovam.giphygallery.R
import com.poovam.giphygallery.main.viewmodel.MainViewModel
import com.poovam.giphygallery.main.viewmodel.Page
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter = MainViewPagerAdapter(viewModel.pages)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(viewModel.pages[position].stringRes)
        }.attach()
    }

    //TODO favourites fragment loading is laggy check it
    private inner class MainViewPagerAdapter(private val pages: List<Page<out Fragment>>) :
        FragmentStateAdapter(this) {

        override fun getItemCount(): Int = pages.size

        override fun createFragment(position: Int): Fragment =
            pages[position].fragment.newInstance()
    }
}
