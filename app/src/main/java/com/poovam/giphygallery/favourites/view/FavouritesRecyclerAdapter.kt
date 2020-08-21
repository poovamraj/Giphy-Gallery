package com.poovam.giphygallery.favourites.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poovam.giphygallery.common.view.GifViewHolder
import com.poovam.giphygallery.common.view.GifViewModel
import com.poovam.giphygallery.favourites.model.db.Favourite

class FavouritesRecyclerAdapter : RecyclerView.Adapter<GifViewHolder>() {

    var dataSet: List<Favourite> = ArrayList()

    var onFavouriteClicked: ((gifId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder.createViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val data = dataSet[position]
        holder.bindView(GifViewModel(data.id, data.originalGifUrl, data.previewGifUrl, true))
        holder.onFavouriteClicked =
            { gifViewModel, _ -> onFavouriteClicked?.invoke(gifViewModel.id) }
    }

}