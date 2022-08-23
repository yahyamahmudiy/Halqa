package com.example.halqa.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.halqa.model.Item

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPhotoHomeDao(): ItemDao

    companion object{
        @Volatile
        private var DB_INSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if (DB_INSTANCE == null){
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DB_ITEM"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}