package com.poovam.giphygallery.webservice.model

data class GiphyResponse(
    val data: List<GifData>,
    val pagination: Pagination
)