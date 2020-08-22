package com.poovam.giphygallery.favourites.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.poovam.giphygallery.R
import com.poovam.giphygallery.common.view.GifPopupView
import com.poovam.giphygallery.favourites.viewmodel.FavouritesViewModel
import kotlinx.android.synthetic.main.trending_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO Test rotation handled properly
//TODO show empty view using lottie
class FavouritesFragment : Fragment() {

    private val viewModel by viewModel<FavouritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = FavouritesRecyclerAdapter()
        val layoutManager = GridLayoutManager(activity, 2)
        view.recyclerView.layoutManager = layoutManager
        view.recyclerView.adapter = adapter

        adapter.onFavouriteClicked = this::onFavouriteClicked
        adapter.onGifClicked = { onGifClicked(it.previewGifUrl, it.originalGifUrl) }

        viewModel.favourites.observe(viewLifecycleOwner) {
            it?.let {
                adapter.dataSet = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onFavouriteClicked(gifId: String) {
        viewModel.removeFavouriteById(gifId)
    }

    private fun onGifClicked(previewGifUrl: String, originalGifUrl: String) {
        GifPopupView.newInstance(previewGifUrl, originalGifUrl).show(childFragmentManager, null)
    }
}