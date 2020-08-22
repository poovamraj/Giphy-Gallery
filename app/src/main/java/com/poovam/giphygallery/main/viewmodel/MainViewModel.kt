package com.poovam.giphygallery.main.viewmodel

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.poovam.giphygallery.R
import com.poovam.giphygallery.favourites.view.FavouritesFragment
import com.poovam.giphygallery.trending.view.TrendingAndSearchFragment
import org.koin.dsl.module

val mainViewModelModule = module {
    factory { MainViewModel() }
}

/**
 * This is provided as [ViewModel] mainly to handle configuration changes
 */
class MainViewModel : ViewModel() {

    val pages = listOf(
        Page(TrendingAndSearchFragment::class.java, R.string.trending),
        Page(FavouritesFragment::class.java, R.string.favourites)
    )
}

/**
 * This model is used to maintain association between the fragment and its title
 * This way, mistake cannot be made with respect to providing one less title or fragment
 * Since fragment shouldn't have custom constructor this shouldn't be an issue
 * Also used Generic in a way that only Fragment can be provided
 */
data class Page<T : Fragment>(val fragment: Class<T>, @StringRes val stringRes: Int)