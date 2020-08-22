package com.poovam.giphygallery

import android.app.Application
import com.poovam.giphygallery.common.db.dbModule
import com.poovam.giphygallery.webservice.giphyApiModule
import com.poovam.giphygallery.common.network.retrofitModule
import com.poovam.giphygallery.favourites.model.favouriteRepositoryModule
import com.poovam.giphygallery.favourites.viewmodel.favouritesViewModelModule
import com.poovam.giphygallery.main.viewmodel.mainViewModelModule
import com.poovam.giphygallery.trending.repository.gifDataSourceModule
import com.poovam.giphygallery.trending.repository.trendingAndSearchRepositoryModule
import com.poovam.giphygallery.trending.viewmodel.trendingAndSearchViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GiphyGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GiphyGalleryApplication)
            modules(
                retrofitModule,
                giphyApiModule,
                gifDataSourceModule,
                trendingAndSearchViewModelModule,
                trendingAndSearchRepositoryModule,
                dbModule,
                favouriteRepositoryModule,
                favouritesViewModelModule,
                mainViewModelModule
            )
        }
    }
}