package com.poovam.giphygallery.trending.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.poovam.giphygallery.R
import com.poovam.giphygallery.common.view.GifViewHolder
import com.poovam.giphygallery.common.view.GifViewModel
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchModel
import com.poovam.giphygallery.webservice.model.GifData


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

class GifRecyclerAdapter : PagedListAdapter<TrendingAndSearchModel, GifViewHolder>(
    DIFF_CALLBACK
) {

    /**
     * Favourites is not provided along with the ViewModel since Android Paging Library currently
     * doesn't support merging data from different sources (i.e) favourites from DB and GifData from Webservice
     */
    var favourites: List<Favourite> = ArrayList()

    var onFavouriteClicked: ((TrendingAndSearchModel, setToFavourite: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_view, parent, false)
        return GifViewHolder(view)
    }


    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bindView(
                GifViewModel(
                    data.id,
                    data.originalUrl,
                    data.previewImageUrl,
                    favourites.find { it.id == data.id } != null)
            )
            holder.onFavouriteClicked = { vm, setToFavourite ->
                onFavouriteClicked?.invoke(
                    TrendingAndSearchModel(
                        vm.id,
                        vm.originalUrl,
                        vm.previewImageUrl
                    ), setToFavourite
                )
            }
        } else {
            //TODO handle loading UI
        }
    }

}