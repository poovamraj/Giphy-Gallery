package com.poovam.giphygallery.common.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    factory { provideOkHttpClient() }
    single { provideRetrofit(get()) }
}

//TODO Make this environment specific
const val API_KEY = "STrTE2Xr1i9Ae8IFIYI27cbeX55JeZA0"

const val API_URL = "https://api.giphy.com/v1/gifs/"

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    return Retrofit.Builder().baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

