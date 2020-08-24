package com.poovam.giphygallery.favourites.model

import android.content.Context
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.poovam.giphygallery.common.db.AppDatabase
import com.poovam.giphygallery.common.localstorage.ImageStorageManager
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.favourites.model.db.FavouriteDao
import com.poovam.giphygallery.common.network.ImageDownloader
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module
import java.io.File


val favouriteRepositoryModule = module {
    fun provideFavouriteRepository(appDatabase: AppDatabase): FavouriteRepository {
        return FavouriteRepository(appDatabase.favouritesDao())
    }

    factory { provideFavouriteRepository(get()) }
}

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    companion object {
        const val FOLDER_NAME = "favourites"
        const val EXTENSION = ".gif"
    }

    suspend fun setFavourite(context: Context, favourite: Favourite) {
        favouriteDao.save(favourite)
        ImageDownloader.downloadGif(context, favourite.previewGifUrl, { image ->
            val path = storeImageInInternalStorage(context, favourite.id, image)
            path?.let {
                favouriteDao.updateLocalPath(favourite.id, it)
            }
        }) {
            it.printStackTrace()
        }
    }

    suspend fun removeFavouriteById(context: Context, favouriteId: String) {
        favouriteDao.deleteById(favouriteId)
        deleteImageFromInternalStorage(context, favouriteId)
    }

    fun getFavourites(): Flow<List<Favourite>> = favouriteDao.getAllFavourites()

    private suspend fun storeImageInInternalStorage(
        context: Context,
        id: String,
        image: GifDrawable
    ): String? {
        val imageName = getImageNameForId(id)
        val path = getFavouritesInternalStorageLocation(context)
        return if (ImageStorageManager.writeGif(image.buffer, path, imageName)) {
            return path + File.separator + imageName
        } else {
            null
        }
    }

    private suspend fun deleteImageFromInternalStorage(context: Context, id: String) {
        ImageStorageManager.deleteGif(
            getFavouritesInternalStorageLocation(context),
            getImageNameForId(id)
        )
    }

    private fun getFavouritesInternalStorageLocation(context: Context): String {
        return context.filesDir.absolutePath + File.separator + FOLDER_NAME
    }

    private fun getImageNameForId(id: String): String {
        return id + EXTENSION
    }
}