package com.poovam.giphygallery.favourites.model.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(gif: Favourite)

    @Query("DELETE FROM FAVOURITE WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM FAVOURITE")
    fun getAllFavourites(): Flow<List<Favourite>>

    @Query("UPDATE FAVOURITE SET localPath = :localPath  WHERE id = :id ")
    suspend fun updateLocalPath(id: String, localPath: String)
}