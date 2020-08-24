package com.poovam.giphygallery.common.network

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandler {
    companion object {
        const val NO_INTERNET = "No Internet Connection"

        fun handleException(e: Exception): String {
            return when (e) {
                is HttpException -> getErrorMessage(e.code())
                is SocketTimeoutException -> getErrorMessage(408)
                is UnknownHostException -> getErrorMessage(999)
                else -> getErrorMessage(Int.MAX_VALUE)
            }
        }

        private fun getErrorMessage(code: Int): String {
            return when (code) {
                408 -> "Timeout"
                401 -> "Unauthorised"
                404 -> "Not found"
                999 -> NO_INTERNET
                else -> "Something went wrong"
            }
        }
    }
}