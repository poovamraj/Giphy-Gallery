package com.poovam.giphygallery.common.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.poovam.giphygallery.R
import kotlinx.android.synthetic.main.gif_item_view.view.*


data class GifItemViewModel(
    val id: String,
    val originalUrl: String,
    val previewImageUrl: String,
    val isFavourite: Boolean
)

//TODO on Tap we should implement a view to come like instagram with share option on and go to url
//TODO Going to Giphy page should be done using chrome
//TODO show favourite button after image is loaded?
data class GifItemViewHolder(
    val view: View,
    val gifHolder: ImageView = view.gifHolder,
    val favourite: ImageView = view.favourite
) : RecyclerView.ViewHolder(view) {

    companion object {
        fun createViewHolder(parent: ViewGroup): GifItemViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.gif_item_view, parent, false)
            return GifItemViewHolder(view)
        }
    }

    var onFavouriteClicked: ((GifItemViewModel, setToFavourite: Boolean) -> Unit)? = null

    var onGifClicked: ((GifItemViewModel) -> Unit)? = null

    fun bindView(viewModel: GifItemViewModel) {
        if (viewModel.isFavourite) {
            favourite.setImageResource(R.drawable.ic_heart_on)
        } else {
            favourite.setImageResource(R.drawable.ic_heart_off)
        }

        favourite.setOnClickListener {
            onFavouriteClicked?.invoke(viewModel, !viewModel.isFavourite)
        }

        view.setOnClickListener { onGifClicked?.invoke(viewModel) }

        val shimmerDrawable = ShimmerView(view.context).getRandomShimmerColor(
            ShimmerView.ShimmerColor.values().random()
        )

        ImageRenderer.render(
            view.context,
            null,
            viewModel.previewImageUrl,
            gifHolder,
            shimmerDrawable
        )
    }
}