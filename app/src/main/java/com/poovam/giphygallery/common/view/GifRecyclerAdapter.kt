package com.poovam.giphygallery.common.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.poovam.giphygallery.R
import com.poovam.giphygallery.webservice.model.GifData
import kotlinx.android.synthetic.main.gif_view.view.*


private val DIFF_CALLBACK: DiffUtil.ItemCallback<GifData> =
    object : DiffUtil.ItemCallback<GifData>() {

        override fun areItemsTheSame(
            oldItem: GifData,
            newItem: GifData
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: GifData,
            newItem: GifData
        ): Boolean = oldItem == newItem
    }

/**
 * [fragmentLifeCycle] is used here since Glide works internally with this lifecycle and
 * it would be better to clear resources held by [Glide] using the fragmentLifeCycle than using
 * a context
 */
class GifRecyclerAdapter(private val fragmentLifeCycle: Fragment) :
    PagedListAdapter<GifData, GifViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_view, parent, false)
        return GifViewHolder(view)
    }


    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            holder.favourite.setOnClickListener {
                Toast.makeText(
                    holder.favourite.context,
                    "Favourite ${holder.adapterPosition}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Glide.with(fragmentLifeCycle)
                .load(data.images.previewGif.url)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)//TODO move this config away from view
                .into(holder.gifHolder)
        } else {
            //TODO handle loading UI
        }
    }

}

data class GifViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val gifHolder: ImageView = itemView.gifHolder
    val favourite: ImageView = itemView.favourite
}