package com.poovam.giphygallery.common.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poovam.giphygallery.favourites.model.db.Favourite
import com.poovam.giphygallery.favourites.model.db.FavouriteDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "app-database")
            .build()
    }

    single { provideDatabase(androidApplication()) }
}

@Database(
    entities = [Favourite::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesDao(): FavouriteDao
}