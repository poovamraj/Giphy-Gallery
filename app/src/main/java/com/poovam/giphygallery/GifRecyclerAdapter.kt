package com.poovam.giphygallery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.gif_view.view.*

class GifRecyclerAdapter(private val fragmentForLifecycle: Fragment) :
    RecyclerView.Adapter<GifViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_view, parent, false)
        return GifViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 200
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.favourite.setOnClickListener {
            Toast.makeText(
                fragmentForLifecycle.context,
                "Favourite ${holder.adapterPosition}",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (position % 2 == 0) {
            Glide.with(fragmentForLifecycle)
                .load("https://media2.giphy.com/media/kBZBlLVlfECvOQAVno/200.gif?cid=8fbbe1ec7dc4eb11eed81284efbde00a46cc2980b1a49ce1&rid=200.gif")
                .into(holder.gifHolder)
        } else {
            Glide.with(fragmentForLifecycle)
                .load("https://media3.giphy.com/media/WqdHCpfddnPTQI6PtV/200w.gif?cid=8fbbe1ec583e7e00776d360154e4750b547856e348e494de&rid=200w.mp4")
                .into(holder.gifHolder)
        }
    }

}

data class GifViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val gifHolder: ImageView = itemView.gifHolder
    val favourite: ImageView = itemView.favourite
}