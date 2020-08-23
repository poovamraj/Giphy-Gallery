package com.poovam.giphygallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.poovam.giphygallery.common.network.Error
import com.poovam.giphygallery.common.network.Loaded
import com.poovam.giphygallery.common.network.Loading
import com.poovam.giphygallery.common.network.NetworkState
import com.poovam.giphygallery.trending.repository.GiphyDataSource
import com.poovam.giphygallery.webservice.GiphyApi
import com.poovam.giphygallery.webservice.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import java.lang.IllegalStateException

class GiphyDataSourceUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val networkState = MutableLiveData<NetworkState>()

    private val mockGiphyApi = mockk<GiphyApi>()

    private class MockInitialCallback(
        private val testCases: (
            data: List<GifData>, previousPageKey: Long?, nextPageKey: Long?
        ) -> Unit
    ) :
        PageKeyedDataSource.LoadInitialCallback<Long, GifData>() {
        override fun onResult(
            data: MutableList<GifData>,
            position: Int,
            totalCount: Int,
            previousPageKey: Long?,
            nextPageKey: Long?
        ) {
        }

        override fun onResult(
            data: MutableList<GifData>,
            previousPageKey: Long?,
            nextPageKey: Long?
        ) {
            testCases(data, previousPageKey, nextPageKey)
        }

    }

    private val result = listOf(
        GifData(
            "fake-id",
            "fake-post-url",
            "fake-rating",
            Images(Image("url"), Image("url"), Image("url"), Image("url"), Image("url"))
        )
    )

    private lateinit var trendingDataSource: GiphyDataSource

    private lateinit var searchingDataSource: GiphyDataSource

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        val dispatcher = TestCoroutineDispatcher()
        val testCoroutineScope = TestCoroutineScope(dispatcher)

        Dispatchers.setMain(dispatcher)
        trendingDataSource =
            GiphyDataSource(mockGiphyApi, "", networkState, testCoroutineScope)
        searchingDataSource = GiphyDataSource(
            mockGiphyApi,
            "Searching",
            networkState,
            testCoroutineScope
        )
    }

    @Test
    fun testLoadInitialForPagination_And_trendingLoadedWhenSearchIsEmpty() {
        val initialParams = PageKeyedDataSource.LoadInitialParams<Long>(50, false)
        coEvery { mockGiphyApi.getTrendingGifs(0) } returns GiphyResponse(
            result,
            Pagination(100, 50, 0)
        )
        val callback = MockInitialCallback { data, previousPageKey, nextPageKey ->
            assertThat(data, `is`(result))
            assertThat(nextPageKey, `is`(1))
            assertNull(previousPageKey)
        }
        trendingDataSource.loadInitial(initialParams, callback)
    }

    @Test
    fun testForSearchResponseLoadedIfQueryIsNotEmpty() {
        val initialParams = PageKeyedDataSource.LoadInitialParams<Long>(50, false)
        coEvery { mockGiphyApi.searchGifs(any(), any()) } returns GiphyResponse(
            result,
            Pagination(100, 50, 0)
        )
        val callback = MockInitialCallback { data, _, _ ->
            assertThat(data, `is`(result))
        }
        searchingDataSource.loadInitial(initialParams, callback)
    }

    @Test
    fun testNetworkStateProvidesLoadingAndLoaded() {
        val initialParams = PageKeyedDataSource.LoadInitialParams<Long>(50, false)
        coEvery { mockGiphyApi.searchGifs(any(), any()) } returns GiphyResponse(
            result,
            Pagination(100, 50, 0)
        )
        val states = arrayOf(Loading, Loaded)
        var counter = 0
        networkState.observeForever {
            assertThat(it, `is`(states[counter]))
            counter++
        }
        trendingDataSource.loadInitial(initialParams, MockInitialCallback { _, _, _ -> })
    }

    @Test
    fun testErrorIsThrownInNetworkState() {
        val exception = IllegalStateException()
        val initialParams = PageKeyedDataSource.LoadInitialParams<Long>(50, false)
        coEvery { mockGiphyApi.searchGifs(any(), any()) } throws exception
        val states = arrayOf(Loading, Error(exception, "Error at loadInitial"))
        var counter = 0
        networkState.observeForever {
            assertThat(it, `is`(states[counter]))
            counter++
        }
        trendingDataSource.loadInitial(initialParams, MockInitialCallback { _, _, _ -> })
    }


    @Test
    fun testLoadAfterFor_loadNextPage_andNotToLoadWhenLimitIsReached() {
        coEvery { mockGiphyApi.getTrendingGifs(any()) } returns GiphyResponse(
            result,
            Pagination(100, 100, 0)
        )
        val params = PageKeyedDataSource.LoadParams<Long>(1, 50)
        val callback = object : PageKeyedDataSource.LoadCallback<Long, GifData>() {
            override fun onResult(data: MutableList<GifData>, adjacentPageKey: Long?) {
                assertThat(data, `is`(result))
                assertThat(adjacentPageKey, `is`(2))
            }
        }
        trendingDataSource.loadAfter(params, callback)

        coEvery { mockGiphyApi.getTrendingGifs(any()) } returns GiphyResponse(
            result,
            Pagination(100, 100, 0)
        )

        val limitReachedParams = PageKeyedDataSource.LoadParams<Long>(2, 50)
        val limitReachedCallBack = object : PageKeyedDataSource.LoadCallback<Long, GifData>() {
            override fun onResult(data: MutableList<GifData>, adjacentPageKey: Long?) {
                assertThat(data, `is`(result))
                assertNull(adjacentPageKey)
            }
        }
        trendingDataSource.loadAfter(limitReachedParams, limitReachedCallBack)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testLoadBeforeFor_loadPrevious_andNotToLoadWhenLimitIsReached() {
        coEvery { mockGiphyApi.getTrendingGifs(any()) } returns GiphyResponse(
            result,
            Pagination(100, 100, 0)
        )
        val params = PageKeyedDataSource.LoadParams<Long>(1, 50)
        val callback = object : PageKeyedDataSource.LoadCallback<Long, GifData>() {
            override fun onResult(data: MutableList<GifData>, adjacentPageKey: Long?) {
                assertThat(data, `is`(result))
                assertThat(adjacentPageKey, `is`(0))
            }
        }
        trendingDataSource.loadBefore(params, callback)

        coEvery { mockGiphyApi.getTrendingGifs(any()) } returns GiphyResponse(
            result,
            Pagination(100, 100, 0)
        )

        val limitReachedParams = PageKeyedDataSource.LoadParams<Long>(0, 50)
        val limitReachedCallBack = object : PageKeyedDataSource.LoadCallback<Long, GifData>() {
            override fun onResult(data: MutableList<GifData>, adjacentPageKey: Long?) {
                assertThat(data, `is`(result))
                assertNull(adjacentPageKey)
            }
        }
        trendingDataSource.loadBefore(limitReachedParams, limitReachedCallBack)
    }
}