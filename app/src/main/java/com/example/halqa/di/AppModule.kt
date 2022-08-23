package com.example.halqa.di

import android.app.Application
import com.example.halqa.db.AppDatabase
import com.example.halqa.db.ItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Room related
     */

    @Provides
    @Singleton
    fun appDatabase(context: Application): AppDatabase {
        return AppDatabase.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun dao(appDatabase: AppDatabase): ItemDao {
        return appDatabase.getPhotoHomeDao()
    }

}