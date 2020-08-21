package com.poovam.giphygallery.trending.repository

import androidx.lifecycle.LiveData
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

    val gifSource: LiveData<PagedList<GifData>>

    val networkState: LiveData<NetworkState>

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(GiphyDataSource.PAGE_SIZE)
            .build()
        gifSource = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        networkState = dataSourceFactory.getErrorData()
    }

    fun search(query: String?) {
        dataSourceFactory.searchQuery = query
    }
}