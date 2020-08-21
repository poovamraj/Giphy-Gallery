package com.poovam.giphygallery.common.view

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.varunest.sparkbutton.SparkButton
import kotlinx.android.synthetic.main.gif_view.view.*


data class GifViewModel(
    val id: String,
    val originalUrl: String,
    val previewImageUrl: String,
    val isFavourite: Boolean
)


data class GifViewHolder(
    val view: View,
    val gifHolder: ImageView = view.gifHolder,
    val favourite: SparkButton = view.favourite
) : RecyclerView.ViewHolder(view) {

    var onFavouriteClicked: ((GifViewModel, setToFavourite: Boolean) -> Unit)? = null

    fun bindView(viewModel: GifViewModel){
        favourite.isChecked = viewModel.isFavourite

        favourite.setOnClickListener {
            onFavouriteClicked?.invoke(viewModel, !viewModel.isFavourite)
            favourite.playAnimation()
        }

        Glide.with(view.context)
            .load(viewModel.previewImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)//TODO move this config away from view
            .into(gifHolder)
    }
}