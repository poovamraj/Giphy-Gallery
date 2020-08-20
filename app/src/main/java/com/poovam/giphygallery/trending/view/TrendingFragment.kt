package com.poovam.giphygallery.trending.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.poovam.giphygallery.GifRecyclerAdapter
import com.poovam.giphygallery.R
import kotlinx.android.synthetic.main.trending_fragment.view.*


//TODO implement refresh
class TrendingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trending_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        view.recyclerView.adapter = GifRecyclerAdapter(this)
    }
}