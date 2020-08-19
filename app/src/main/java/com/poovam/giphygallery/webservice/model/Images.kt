package com.poovam.giphygallery.webservice.model

data class Images(
    val fixedHeight: FixedHeightImage,
    val downsizedLarge: DownsizedLargeImage
)

data class FixedHeightImage(
    val url: String
)

data class DownsizedLargeImage(
    val url: String
)