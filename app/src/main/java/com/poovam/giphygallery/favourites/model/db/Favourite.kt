package com.poovam.giphygallery.favourites.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourite (
    @PrimaryKey val id: String,
    val originalGifUrl: String,
    val previewGifUrl: String
)