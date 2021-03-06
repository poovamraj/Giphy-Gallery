package com.poovam.giphygallery.favourites.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.poovam.giphygallery.R
import com.poovam.giphygallery.common.view.GifPopupView
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.favourites.viewmodel.FavouritesViewModel
import kotlinx.android.synthetic.main.favourites_fragment.*
import kotlinx.android.synthetic.main.favourites_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private val viewModel by viewModel<FavouritesViewModel>()

    private val adapter = FavouritesRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        view.recyclerView.adapter = adapter
        adapter.onFavouriteClicked = this::onFavouriteClicked
        adapter.onGifClicked = { onGifClicked(it.previewGifUrl, it.originalGifUrl, it.localPath) }

        viewModel.favourites.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                setEmptyView()
            } else {
                setFavourites(it)
            }
        }
    }

    private fun setFavourites(favourites: List<Favourite>) {
        emptyAnimationView.visibility = View.GONE
        emptyText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.dataSet = favourites
        adapter.notifyDataSetChanged()
    }

    //TODO Test this case in integration testing
    private fun setEmptyView() {
        recyclerView.visibility = View.GONE
        emptyAnimationView.visibility = View.VISIBLE
        emptyText.visibility = View.VISIBLE
        emptyAnimationView.playAnimation()
    }

    private fun onFavouriteClicked(gifId: String) {
        context?.let { viewModel.removeFavouriteById(it, gifId) }
    }

    private fun onGifClicked(previewGifUrl: String, originalGifUrl: String, localPath: String?) {
        GifPopupView.newInstance(previewGifUrl, originalGifUrl, localPath).show(childFragmentManager, null)
    }
}