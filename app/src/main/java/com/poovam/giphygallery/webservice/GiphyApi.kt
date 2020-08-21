package com.poovam.giphygallery.webservice

import com.poovam.giphygallery.common.network.API_KEY
import com.poovam.giphygallery.trending.repository.GiphyDataSource.Companion.PAGE_SIZE
import com.poovam.giphygallery.webservice.model.GiphyResponse
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

val giphyApiModule = module {
    fun provideGiphyAPi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }
    factory { provideGiphyAPi(get()) }
}

interface GiphyApi {

    @GET("trending?api_key=$API_KEY&limit=$PAGE_SIZE")
    suspend fun getTrendingGifs(@Query("offset") offset: Long): GiphyResponse


    @GET("search?api_key=$API_KEY&limit=$PAGE_SIZE")
    suspend fun searchGifs(@Query("offset") offset: Long, @Query("q") query: String): GiphyResponse
}