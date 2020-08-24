package com.poovam.giphygallery.common.network

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class ImageDownloader {
    companion object {
        suspend fun downloadGif(
            context: Context,
            url: String,
            onSuccess: suspend (GifDrawable) -> Unit,
            onFailure: suspend (Exception) -> Unit
        ) {
            Glide.with(context)
                .asGif()
                .load(url)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (e != null) {
                                onFailure(e)
                            } else {
                                onFailure(GlideException("Unknown error"))
                            }
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (resource != null) {
                                onSuccess(resource)
                            } else {
                                onFailure(GlideException("Resource returned null"))
                            }
                        }
                        return false
                    }

                })
                .submit()
        }
    }
}