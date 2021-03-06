package com.poovam.giphygallery.trending.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieDrawable.RESTART
import com.poovam.giphygallery.R
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.ErrorHandler
import com.poovam.giphygallery.common.network.Loaded
import com.poovam.giphygallery.common.network.Loading
import com.poovam.giphygallery.common.view.GifPopupView
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchModel
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchViewModel
import kotlinx.android.synthetic.main.trending_fragment.*
import kotlinx.android.synthetic.main.trending_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrendingAndSearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModel<TrendingAndSearchViewModel>()

    private val adapter = GifRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trending_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = GridLayoutManager(activity, 2)
        view.recyclerView.layoutManager = layoutManager
        view.recyclerView.adapter = adapter

        //TODO handle proper difference between search providing 0 values and initial loading
        viewModel.gifs.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.favourites.observe(viewLifecycleOwner) {
            it?.let { adapter.favourites = it }
            adapter.notifyDataSetChanged()
        }

        view.swipeRefreshLayout.setOnRefreshListener { onRefreshed() }

        adapter.onFavouriteClicked = this::onFavouriteClicked

        adapter.onGifClicked = this::onGifClicked

        viewModel.networkState.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    Loading -> {
                        onDataLoading(adapter.itemCount)
                    }
                    Loaded -> {
                        onDataLoaded()
                    }
                    is Error -> {
                        onErrorLoadingPage(it.errorMessage)
                    }
                }
            }
        }

        view.searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onSearched(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onSearched(newText)
        return true
    }

    private fun onSearched(query: String?) {
        viewModel.searchQuery = query
        onDataLoading(0)
    }

    private fun onFavouriteClicked(gifData: TrendingAndSearchModel, setToFavourite: Boolean) {
        context?.let { viewModel.onFavouriteClicked(it, gifData, setToFavourite) }
    }

    private fun onRefreshed() {
        viewModel.refresh()
    }

    private fun onDataLoading(dataSetSize: Int) {
        hideAnimations()
        showRecyclerView()
        if (dataSetSize == 0) {
            swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun onDataLoaded() {
        hideAnimations()
        showRecyclerView()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun onErrorLoadingPage(errorMessage: String) {
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        if (errorMessage == ErrorHandler.NO_INTERNET) {
            recyclerView.visibility = View.GONE
            playNoInternetAnimation()
        }
    }

    private fun onGifClicked(viewModel: TrendingAndSearchModel) {
        GifPopupView
            .newInstance(viewModel.previewImageUrl, viewModel.originalUrl, null)
            .show(childFragmentManager, null)
    }

    private fun hideAnimations() {
        noInternetAnimation.visibility = View.GONE
        noInternetText.visibility = View.GONE
    }

    private fun playNoInternetAnimation() {
        noInternetAnimation.visibility = View.VISIBLE
        noInternetText.visibility = View.VISIBLE
        noInternetAnimation.repeatMode = RESTART
        noInternetAnimation.playAnimation()
    }

    private fun showRecyclerView() {
        recyclerView.visibility = View.VISIBLE
    }
}