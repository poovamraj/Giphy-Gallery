package com.poovam.giphygallery.favourites.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poovam.giphygallery.common.view.GifItemViewHolder
import com.poovam.giphygallery.common.view.GifItemViewModel
import com.poovam.giphygallery.favourites.model.db.Favourite

class FavouritesRecyclerAdapter : RecyclerView.Adapter<GifItemViewHolder>() {

    var dataSet: List<Favourite> = ArrayList()

    var onFavouriteClicked: ((gifId: String) -> Unit)? = null

    var onGifClicked: ((Favourite) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifItemViewHolder {
        return GifItemViewHolder.createViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: GifItemViewHolder, position: Int) {
        val data = dataSet[position]
        holder.bindView(
            GifItemViewModel(
                data.id,
                data.originalGifUrl,
                data.previewGifUrl,
                data.localPath,
                true
            )
        )
        holder.onFavouriteClicked =
            { gifViewModel, _ -> onFavouriteClicked?.invoke(gifViewModel.id) }
        holder.onGifClicked =
            { it ->
                onGifClicked?.invoke(
                    Favourite(
                        it.id,
                        it.originalUrl,
                        it.previewImageUrl,
                        it.localPath
                    )
                )
            }
    }

}