package com.poovam.giphygallery.trending.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.ErrorHandler
import com.poovam.giphygallery.favourites.model.FavouriteRepository
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.trending.repository.TrendingAndSearchRepository
import com.poovam.giphygallery.webservice.model.GifData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.lang.Exception


val trendingAndSearchViewModelModule = module {
    viewModel { TrendingAndSearchViewModel(get(), get()) }
}

class TrendingAndSearchViewModel(
    private val trendingAndSearchRepository: TrendingAndSearchRepository<TrendingAndSearchModel>,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    private val tag = TrendingAndSearchViewModel::class.simpleName

    init {
        trendingAndSearchRepository.loadDataSourceAs { buildTrendingAndSearchModel(it) }
    }

    /**
     * Could not merge Gif and Favourites using [MediatorLiveData] since Android Paging Library
     * does not support this.
     *
     * Merging from 2 different data source is currently not supported in Android Paging Library
     * Need to check InMemory Implementation
     */
    val gifs = trendingAndSearchRepository.getGifSource()

    val favourites = favouriteRepository.getFavourites().asLiveData()

    /**
     * Search query value is maintained in ViewModel since [androidx.appcompat.widget.SearchView.OnQueryTextListener.onQueryTextChange]
     * will be called on rotation which causes the data source to be invalidated. Hence we are
     * invalidating only if search query is different than before
     *
     * Initial value set to "" since [androidx.appcompat.widget.SearchView.OnQueryTextListener.onQueryTextChange]
     * sends this on rotation and if initial value is null it will cause rotation at first rotation alone.
     * Hence setting to ""
     */
    var searchQuery: String? = ""
        set(value) {
            if (field != value) {
                field = value
                search(value)
            }
        }

    val networkState = Transformations.map(trendingAndSearchRepository.getNetworkState()) {
        return@map if (it is Error) {
            Log.e(tag, it.errorMessage, it.exception)
            Error(it.exception, ErrorHandler.handleException(it.exception))
        } else {
            it
        }
    }

    //TODO Debounce here
    private fun search(query: String?) {
        trendingAndSearchRepository.search(query)
    }

    fun refresh() {
        trendingAndSearchRepository.refresh()
    }

    /**
     * Marks a Gif as Favourite or Removes it
     * not operating on [Dispatchers.IO] since Room automatically handles this
     */
    fun onFavouriteClicked(viewModel: TrendingAndSearchModel, setToFavourite: Boolean) {
        if (setToFavourite) {
            viewModelScope.launch {
                favouriteRepository.setFavourite(
                    Favourite(viewModel.id, viewModel.originalUrl, viewModel.previewImageUrl)
                )
            }
        } else {
            viewModelScope.launch {
                favouriteRepository.removeFavouriteById(viewModel.id)
            }
        }
    }

    /**
     * Used to build [TrendingAndSearchModel] from [GifData]
     * There are times when Giphy API Response doesn't provide us with a url for image
     * such cases are handled in the exception and logged for analysis
     * This is a rare case (Saw it only for one Gif). but exception caught anyways to avoid app crash
     */
    private fun buildTrendingAndSearchModel(gifData: GifData): TrendingAndSearchModel? {
        return try {
            TrendingAndSearchModel(
                gifData.id,
                gifData.images.fixedHeight.url,
                gifData.images.fixedWidthDownsampled.url,
            )
        } catch (e: Exception) {
            Log.e(tag, "Webservice has provided data as null", e)
            null
        }
    }
}

/**
 * Model used to transfer Gif data. This does not contain favourites in it since Paging Library doesn't
 * allow Merging two different sources. Hence favourites list is passed separately.
 */
data class TrendingAndSearchModel(
    val id: String,
    val originalUrl: String,
    val previewImageUrl: String,
)