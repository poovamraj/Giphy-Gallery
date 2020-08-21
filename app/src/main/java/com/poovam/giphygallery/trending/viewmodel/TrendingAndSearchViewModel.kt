package com.poovam.giphygallery.trending.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.ErrorHandler
import com.poovam.giphygallery.favourites.model.FavouriteRepository
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.trending.repository.TrendingAndSearchRepository
import com.poovam.giphygallery.webservice.model.GifData
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val trendingAndSearchViewModelModule = module {
    viewModel { TrendingAndSearchViewModel(get(), get()) }
}

class TrendingAndSearchViewModel(
    private val repository: TrendingAndSearchRepository,
    private val favouriteRepository: FavouriteRepository
) :
    ViewModel() {

    init {
        repository.loadDataSource()
    }
    //TODO Transform and send only required data as a seperate viewmodel
    //TODO rotation not handled properly
    val gifs = repository.getGifSource()

    val favourites = favouriteRepository.getFavourites().asLiveData()

    val networkState = Transformations.map(repository.getNetworkState()) {
        return@map if (it is Error) {
            Log.e(
                TrendingAndSearchViewModel::class.simpleName,
                it.errorMessage + ":" + it.exception.stackTrace
            )
            Error(it.exception, ErrorHandler.handleException(it.exception))
        } else {
            it
        }
    }

    //TODO Debounce here
    fun search(query: String?) {
        repository.search(query)
    }

    fun onFavouriteClicked(gifData: GifData, isFavourite: Boolean) {
        if(isFavourite){
            viewModelScope.launch {
                favouriteRepository.setFavourite(
                    Favourite(
                        gifData.id,
                        gifData.url,
                        gifData.images.previewGif.url
                    )
                )
            }
        } else {
            viewModelScope.launch {
                favouriteRepository.removeFavouriteById(gifData.id)
            }
        }
    }
}