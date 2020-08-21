package com.poovam.giphygallery.trending.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.poovam.giphygallery.common.network.NetworkState
import com.poovam.giphygallery.webservice.model.GifData
import org.koin.dsl.module
import androidx.arch.core.util.Function
import com.poovam.giphygallery.trending.viewmodel.TrendingAndSearchModel

val trendingAndSearchRepositoryModule = module {
    factory { TrendingAndSearchRepository<TrendingAndSearchModel>(get()) }
}

/**
 * Repository pattern is used instead of providing direct access to [GiphyDataSource]
 * since in future there might raise a requirement to store value in DB/Locally during when this
 * abstraction would be useful
 *
 *  //TODO explain why generics is used
 */
class TrendingAndSearchRepository<ToValue>(private val dataSourceFactory: GiphyDataSourceFactory) {

    private val gifSource = MediatorLiveData<PagedList<ToValue>>()

    private val networkState = MediatorLiveData<NetworkState>()

    fun loadDataSourceAs(map: Function<GifData, ToValue>) {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(GiphyDataSource.PAGE_SIZE)
            .build()

        val source = LivePagedListBuilder(
            dataSourceFactory.map(map),
            pagedListConfig
        ).build()

        gifSource.addSource(source) {
            gifSource.value = it
        }
        networkState.addSource(dataSourceFactory.getErrorData()) {
            networkState.value = it
        }
    }

    fun getGifSource(): LiveData<PagedList<ToValue>> {
        return gifSource
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return networkState
    }

    fun search(query: String?) {
        dataSourceFactory.searchQuery = query
    }

    fun refresh() {
        dataSourceFactory.refresh()
    }
}