package com.poovam.giphygallery.common.view

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.poovam.giphygallery.R
import com.varunest.sparkbutton.SparkButton
import kotlinx.android.synthetic.main.gif_view.view.*


data class GifViewModel(
    val id: String,
    val originalUrl: String,
    val previewImageUrl: String,
    val isFavourite: Boolean
)

//TODO on Tap we should implement a view to come like instagram with share option on and go to url
//TODO Going to Giphy page should be done using chrome
//TODO show favourite button after image is loaded?
data class GifViewHolder(
    val view: View,
    val gifHolder: ImageView = view.gifHolder,
    val favourite: SparkButton = view.favourite
) : RecyclerView.ViewHolder(view) {

    companion object {
        fun createViewHolder(parent: ViewGroup): GifViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_view, parent, false)
            return GifViewHolder(view)
        }
    }

    var onFavouriteClicked: ((GifViewModel, setToFavourite: Boolean) -> Unit)? = null

    var onGifClicked: ((GifViewModel) -> Unit)? = null

    fun bindView(viewModel: GifViewModel) {
        favourite.isChecked = viewModel.isFavourite

        favourite.setOnClickListener {
            onFavouriteClicked?.invoke(viewModel, !viewModel.isFavourite)
            favourite.playAnimation()
        }

        view.setOnClickListener { onGifClicked?.invoke(viewModel) }

        ImageRenderer.render(
            view.context,
            null,
            viewModel.previewImageUrl,
            gifHolder
        )
    }
}