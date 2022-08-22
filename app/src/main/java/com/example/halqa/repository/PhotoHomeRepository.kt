package com.example.halqa.repository

import com.example.halqa.db.ItemDao
import com.example.halqa.model.Item
import javax.inject.Inject

class PhotoHomeRepository @Inject constructor(private val photoHomeDao: ItemDao) {

    /**
     *Room db Related
     */

    suspend fun getPhotosFromDB() = photoHomeDao.getFromDB()
    suspend fun insertPhotosToDB(pin: Item) = photoHomeDao.insertToDB(pin)
    suspend fun deletePhotosFromDB() = photoHomeDao.deleteFromDB()
}