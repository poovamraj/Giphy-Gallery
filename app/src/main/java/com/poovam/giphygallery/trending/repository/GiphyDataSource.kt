package com.poovam.giphygallery.trending.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.Loaded
import com.poovam.giphygallery.common.network.Loading
import com.poovam.giphygallery.common.network.NetworkState
import com.poovam.giphygallery.webservice.GiphyApi
import com.poovam.giphygallery.webservice.model.GifData
import com.poovam.giphygallery.webservice.model.GiphyResponse
import kotlinx.coroutines.*
import org.koin.dsl.module
import java.lang.Exception

val gifDataSourceModule = module {
    fun provideDataSourceFactory(giphyApi: GiphyApi): GiphyDataSourceFactory {
        return GiphyDataSourceFactory(giphyApi, Dispatchers.IO)
    }

    factory { provideDataSourceFactory(get()) }
}

class GiphyDataSourceFactory(
    private val giphyApi: GiphyApi,
    private val dispatcher: CoroutineDispatcher
) : DataSource.Factory<Long, GifData>() {

    /**
     * The error faced when loading the pages are posted here
     */
    private val networkState = MutableLiveData<NetworkState>()

    /**
     * Only [create] method should set this when new [DataSource] is created
     * This is used for cases like search and refresh where the datasource will be invalidated
     */
    private var dataSource: GiphyDataSource? = null

    /**
     * Search parameter will be set here and each time it is set the [dataSource] will be invalidated
     * to fetch the response
     */
    var searchQuery: String? = null
        set(value) {
            field = value
            dataSource?.invalidate()
        }

    fun refresh() {
        dataSource?.invalidate()
    }

    fun getErrorData(): LiveData<NetworkState> {
        return networkState
    }

    override fun create(): DataSource<Long, GifData> {
        val newDataSource = GiphyDataSource(giphyApi, searchQuery, networkState, dispatcher)
        dataSource = newDataSource
        return newDataSource
    }
}

class GiphyDataSource(
    private val giphyApi: GiphyApi,
    private val search: String?,
    private val networkState: MutableLiveData<NetworkState>,
    dispatcher: CoroutineDispatcher
) : PageKeyedDataSource<Long, GifData>() {

    companion object {
        const val PAGE_SIZE = 50
        private const val FIRST_PAGE = 0L
    }

    private val coroutineScope = CoroutineScope(dispatcher)

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, GifData>
    ) {
        coroutineScope.launch {
            try {
                val list = getApiData(FIRST_PAGE)
                callback.onResult(list.data, null, FIRST_PAGE + 1)
            } catch (e: Exception) {
                handleException(e, "Error at loadInitial")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, GifData>) {
        coroutineScope.launch {
            try {
                val list = getApiData(params.key)
                val adjacentKey =
                    if (params.key * PAGE_SIZE < list.pagination.totalCount) params.key + 1 else null
                callback.onResult(list.data, adjacentKey)
            } catch (e: Exception) {
                handleException(e, "Error at loadAfter - ${params.key}")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, GifData>) {
        coroutineScope.launch {
            try {
                val list = getApiData(params.key * PAGE_SIZE)
                val adjacentKey = if (params.key > FIRST_PAGE) params.key - 1 else null
                callback.onResult(list.data, adjacentKey)
            } catch (e: Exception) {
                handleException(e, "Error at loadBefore ${params.key}")
            }
        }
    }

    private suspend fun getApiData(key: Long): GiphyResponse {
        networkState.postValue(Loading)
        return if (search.isNullOrEmpty()) {
            giphyApi.getTrendingGifs(key * PAGE_SIZE)
        } else {
            giphyApi.searchGifs(key * PAGE_SIZE, search)
        }.also { networkState.postValue(Loaded) }
    }

    /**
     * Job cancellations are handled internally and occurs when new we move to new [DataSource]
     * Currently there is no requirement to propagate it
     */
    private fun handleException(e: Exception, errorMessage: String) {
        if (e !is CancellationException) {
            networkState.postValue(Error(e, errorMessage))
        }
        e.printStackTrace()
    }

    /**
     * We cancel the network calls if the [DataSource] is [invalidate]
     * We just need to cancel the scope, Retrofit handles network cancellation
     * TODO Check [DataSource] calls invalidate when it is going out of scope (for ex - Fragment is destroyed)
     */
    override fun invalidate() {
        if (coroutineScope.isActive) {
            coroutineScope.cancel()
        }
        super.invalidate()
    }
}