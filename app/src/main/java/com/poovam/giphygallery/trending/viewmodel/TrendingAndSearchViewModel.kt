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
import java.lang.Exception


val trendingAndSearchViewModelModule = module {
    viewModel { TrendingAndSearchViewModel(get(), get()) }
}

//TODO rotation not handled properly
//TODO clean how this class looks
class TrendingAndSearchViewModel(
    private val trendingAndSearchRepository: TrendingAndSearchRepository<TrendingAndSearchModel>,
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    init {
        trendingAndSearchRepository.loadDataSourceAs { buildTrendingAndSearchModel(it) }
    }

    /**
     * Could not merge Gif and Favourites using [MediatorLiveData] since Android Paging Library
     * does not support this.
     *
     * Merging from 2 different data source is currently not supported in Android Paging Library
     *
     */
    val gifs = trendingAndSearchRepository.getGifSource()

    val favourites = favouriteRepository.getFavourites().asLiveData()

    val networkState = Transformations.map(trendingAndSearchRepository.getNetworkState()) {
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
        trendingAndSearchRepository.search(query)
    }

    fun onFavouriteClicked(viewModel: TrendingAndSearchModel, setToFavourite: Boolean) {
        if (setToFavourite) {
            viewModelScope.launch {
                favouriteRepository.setFavourite(
                    Favourite(
                        viewModel.id,
                        viewModel.originalUrl,
                        viewModel.previewImageUrl
                    )
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
                gifData.url,
                gifData.images.fixedWidthDownsampled.url,
            )
        } catch (e: Exception) {
            Log.e(
                TrendingAndSearchViewModel::class.simpleName,
                "Webservice has provided data as null" + ":" + e.stackTrace
            )
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