package com.poovam.giphygallery.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File

/**
 * Abstraction from Glide library. If in future we plan to move away from Glide. Just changing
 * the [render] method will reflect all over the code base
 */
class ImageRenderer {

    companion object {
        fun render(
            context: Context,
            previewUrl: String?,
            originalUrl: String?,
            localPath: String?,
            imageView: ImageView,
            placeholder: Drawable
        ) {
            var glide = Glide.with(context)
                .load(originalUrl)
            val thumbnailRequest: RequestBuilder<Drawable> = Glide
                .with(context)
                .load(previewUrl)

            glide = glide.thumbnail(thumbnailRequest)

            val localRequest: RequestBuilder<Drawable> = Glide
                .with(context)
                .load(localPath)
            glide = glide.thumbnail(localRequest)

            glide.placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }

}