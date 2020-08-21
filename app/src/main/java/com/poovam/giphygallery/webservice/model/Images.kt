package com.poovam.giphygallery.webservice.model

data class Images(
    val fixedHeight: Image,
    val downsizedLarge: Image,
    val downsized: Image,
    val previewGif: Image
)

data class Image(
    val url: String
)
