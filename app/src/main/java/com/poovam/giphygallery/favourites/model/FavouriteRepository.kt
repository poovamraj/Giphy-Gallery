package com.poovam.giphygallery.favourites.model

import com.poovam.giphygallery.common.db.AppDatabase
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.favourites.model.db.FavouriteDao
import com.poovam.giphygallery.webservice.model.GifData
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val favouriteRepositoryModule = module {
    fun provideFavouriteRepository(appDatabase: AppDatabase): FavouriteRepository {
        return FavouriteRepository(appDatabase.favouritesDao())
    }

    factory { provideFavouriteRepository(get()) }
}

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    suspend fun setFavourite(favourite: Favourite) = favouriteDao.save(favourite)

    suspend fun removeFavouriteById(favouriteId: String) = favouriteDao.deleteById(favouriteId)

    fun getFavourites(): Flow<List<Favourite>> = favouriteDao.getAllFavourites()
}