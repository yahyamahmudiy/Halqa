package com.example.halqa.repository

import com.example.halqa.db.ItemDao
import com.example.halqa.model.BookData
import com.example.halqa.model.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    /**
     *Room db Related
     */

    suspend fun getPhotosFromDB() = itemDao.getFromDB()
    suspend fun insertPhotosToDB(pin: Item) = itemDao.insertToDB(pin)
    suspend fun deletePhotosFromDB() = itemDao.deleteFromDB()
    suspend fun createPost(bookData: BookData) = itemDao.createPost(bookData)
    suspend fun getBookAudios(bookName: String) = itemDao.getBookAudios(bookName)
    suspend fun getDownloadId(id: Int) = itemDao.getDownloadId(id)
    suspend fun updateDownload(downloaded: Boolean, downloadID: Long?): Int  = itemDao.updateDownload(downloaded, downloadID)
}