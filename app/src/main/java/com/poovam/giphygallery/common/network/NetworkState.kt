package com.poovam.giphygallery.common.network

import java.lang.Exception

/**
 * Modified version of recommended practice
 * https://github.com/android/architecture-components-samples/blob/master/PagingWithNetworkSample/lib/src/main/java/com/android/example/paging/pagingwithnetwork/reddit/repository/NetworkState.kt
 */

sealed class NetworkState
object Loading : NetworkState()
object Loaded : NetworkState()
data class Error(val exception: Exception, val errorMessage: String): NetworkState()