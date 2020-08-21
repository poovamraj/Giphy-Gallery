package com.poovam.giphygallery.trending.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.poovam.giphygallery.common.network.NetworkState
import com.poovam.giphygallery.webservice.model.GifData
import org.koin.dsl.module

val trendingAndSearchRepositoryModule = module {
    factory { TrendingAndSearchRepository(get()) }
}

/**
 * Repository pattern is used instead of providing direct access to [GiphyDataSource]
 * since in future there might raise a requirement to store value in DB/Locally during when this
 * abstraction would be useful
 */
class TrendingAndSearchRepository(private val dataSourceFactory: GiphyDataSourceFactory) {

    private val gifSource = MediatorLiveData<PagedList<GifData>>()

    private val networkState = MediatorLiveData<NetworkState>()

    fun loadDataSource() {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(GiphyDataSource.PAGE_SIZE)
            .build()
        gifSource.addSource(LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()) {
            gifSource.value = it
        }
        networkState.addSource(dataSourceFactory.getErrorData()) {
            networkState.value = it
        }
    }

    fun getGifSource(): LiveData<PagedList<GifData>> {
        return gifSource
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun search(query: String?) {
        dataSourceFactory.searchQuery = query
    }
}