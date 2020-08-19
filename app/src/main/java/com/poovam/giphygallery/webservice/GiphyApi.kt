package com.poovam.giphygallery.webservice

import com.poovam.giphygallery.common.API_KEY
import com.poovam.giphygallery.webservice.model.GiphyResponse
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.http.GET

val giphyApiModule = module {
    fun provideGiphyAPi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }
    factory { provideGiphyAPi(get()) }
}

interface GiphyApi {

    @GET("trending?api_key=$API_KEY")
    suspend fun getTrendingGifs(): GiphyResponse

}