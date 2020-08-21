package com.poovam.giphygallery.common.network

import retrofit2.HttpException
import java.net.SocketTimeoutException

class ErrorHandler {
    companion object {
        fun handleException(e: Exception): String {
            return when (e) {
                is HttpException -> getErrorMessage(e.code())
                is SocketTimeoutException -> getErrorMessage(408)
                else -> getErrorMessage(Int.MAX_VALUE)
            }
        }

        private fun getErrorMessage(code: Int): String {
            return when (code) {
                408 -> "Timeout"
                401 -> "Unauthorised"
                404 -> "Not found"
                else -> "Something went wrong"
            }
        }
    }
}