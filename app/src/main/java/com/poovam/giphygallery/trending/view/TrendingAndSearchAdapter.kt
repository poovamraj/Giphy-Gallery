package com.poovam.giphygallery.trending.view

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.poovam.giphygallery.common.view.GifItemViewHolder
import com.poovam.giphygallery.common.view.GifItemViewModel
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchModel


private val DIFF_CALLBACK: DiffUtil.ItemCallback<TrendingAndSearchModel> =
    object : DiffUtil.ItemCallback<TrendingAndSearchModel>() {

        override fun areItemsTheSame(
            oldItem: TrendingAndSearchModel,
            newItem: TrendingAndSearchModel
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TrendingAndSearchModel,
            newItem: TrendingAndSearchModel
        ): Boolean = oldItem == newItem
    }

class GifRecyclerAdapter : PagedListAdapter<TrendingAndSearchModel, GifItemViewHolder>(
    DIFF_CALLBACK
) {

    /**
     * Favourites is not provided along with the ViewModel since Android Paging Library currently
     * doesn't support merging data from different sources (i.e) favourites from DB and GifData from Webservice
     */
    var favourites: List<Favourite> = ArrayList()

    var onFavouriteClicked: ((TrendingAndSearchModel, setToFavourite: Boolean) -> Unit)? = null

    var onGifClicked: ((TrendingAndSearchModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifItemViewHolder {
        return GifItemViewHolder.createViewHolder(parent)
    }


    override fun onBindViewHolder(holder: GifItemViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bindView(
                GifItemViewModel(
                    data.id,
                    data.originalUrl,
                    data.previewImageUrl,
                    favourites.find { it.id == data.id } != null)
            )
            holder.onFavouriteClicked = { vm, setToFavourite ->
                onFavouriteClicked?.invoke(
                    TrendingAndSearchModel(vm.id, vm.originalUrl, vm.previewImageUrl),
                    setToFavourite
                )
            }
            holder.onGifClicked = {
                onGifClicked?.invoke(TrendingAndSearchModel(it.id, it.originalUrl, it.previewImageUrl))
            }
        } else {
            //TODO handle loading UI
        }
    }

}