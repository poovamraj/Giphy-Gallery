package com.poovam.giphygallery.trending.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.poovam.giphygallery.R
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.Loaded
import com.poovam.giphygallery.common.network.Loading
import com.poovam.giphygallery.common.view.GifViewModel
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchModel
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchViewModel
import kotlinx.android.synthetic.main.trending_fragment.*
import kotlinx.android.synthetic.main.trending_fragment.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

//TODO implement multi color for refresh spin
class TrendingAndSearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel by viewModel<TrendingAndSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trending_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = GifRecyclerAdapter()
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

        viewModel.networkState.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    Loading ->  { onDataLoading() }
                    Loaded -> { onDataLoaded() }
                    is Error -> { onErrorLoadingPage(it.errorMessage) }
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
        viewModel.search(query)
    }

    private fun onFavouriteClicked(gifData: TrendingAndSearchModel, setToFavourite: Boolean) {
        viewModel.onFavouriteClicked(gifData, setToFavourite)
    }

    private fun onRefreshed() {
        viewModel.refresh()
    }

    //TODO Implement UI
    private fun onDataLoading(){
        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
    }

    //TODO Implement UI
    private fun onDataLoaded() {
        swipeRefreshLayout.isRefreshing = false
    }

    //TODO Implement UI
    private fun onErrorLoadingPage(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}