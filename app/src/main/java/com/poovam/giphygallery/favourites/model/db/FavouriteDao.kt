package com.poovam.giphygallery.favourites.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(gif: Favourite)

    @Query("DELETE FROM FAVOURITE WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM FAVOURITE")
    fun getAllFavourites(): Flow<List<Favourite>>
}