package com.poovam.giphygallery

import android.app.Application
import com.poovam.giphygallery.webservice.giphyApiModule
import com.poovam.giphygallery.common.retrofitModule
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
                giphyApiModule
            )
        }
    }
}