package com.poovam.giphygallery.trending.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.ErrorHandler
import com.poovam.giphygallery.trending.repository.TrendingAndSearchRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val trendingAndSearchViewModelModule = module {
    viewModel { TrendingAndSearchViewModel(get()) }
}

class TrendingAndSearchViewModel(private val repository: TrendingAndSearchRepository) :
    ViewModel() {

    //TODO Transform and send only required data as a seperate viewmodel
    //TODO rotation not handled properly
    val gifs = repository.gifSource

    val networkState = Transformations.map(repository.networkState) {
        return@map if (it is Error) {
            Log.e(TrendingAndSearchViewModel::class.simpleName, it.errorMessage)
            Error(it.exception, ErrorHandler.handleException(it.exception))
        } else {
            it
        }
    }

    //TODO Debounce here
    fun search(query: String?) {
        repository.search(query)
    }

}