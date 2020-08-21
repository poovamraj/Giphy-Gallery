package com.poovam.giphygallery.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.poovam.giphygallery.favourites.model.FavouriteRepository
import com.poovam.giphygallery.favourites.model.favouriteRepositoryModule
import kotlinx.coroutines.launch
import org.koin.dsl.module

val favouritesViewModelModule = module {
    factory { FavouritesViewModel(get()) }
}

class FavouritesViewModel(private val favouritesRepository: FavouriteRepository) : ViewModel() {

    val favourites = favouritesRepository.getFavourites().asLiveData()

    fun removeFavouriteById(gifId: String){
        viewModelScope.launch { favouritesRepository.removeFavouriteById(gifId) }
    }
}