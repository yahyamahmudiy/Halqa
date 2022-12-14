package com.example.halqa.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.model.BookmarkData

@Database(entities = [BookmarkData::class, BookData::class, BookmarkAudioData::class], version = 10)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        private var instance: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DB_ITEM"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase
        }
    }
}