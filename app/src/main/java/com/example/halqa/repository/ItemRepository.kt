package com.example.halqa.repository

import com.example.halqa.db.ItemDao
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.model.BookmarkData
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    /**
     *Room db Related
     */

    suspend fun getBookmarkFromDB() = itemDao.getBookmarkFromDB()
    suspend fun insertBookmarkToDB(bookmark: BookmarkData) = itemDao.insertBookmarkToDB(bookmark)
    suspend fun insertAudioBookmarkToDB(bookmarkAudioData: BookmarkAudioData) =
        itemDao.insertAudioBookmarkToDB(bookmarkAudioData)

    suspend fun deleteBookmarkFromDB() = itemDao.deleteBookmarkFromDB()
    suspend fun createPost(bookData: BookData) = itemDao.createPost(bookData)
    suspend fun getBookAudios(bookName: String) = itemDao.getBookAudios(bookName)
    suspend fun getBookAudiosNotEmptyUrl(bookName: String) =
        itemDao.getBookAudiosNotEmptyUrl(bookName)

    suspend fun getDownloadedBookDataSize(bookName: String, isDownloaded: Boolean) =
        itemDao.getDownloadedBookDataSize(bookName, isDownloaded)

    suspend fun updateDownloadId(id: Int, downloadID: Long) =
        itemDao.updateDownloadId(id, downloadID)

    suspend fun updateDownloadStatus(status: Boolean, downloadID: Long) =
        itemDao.updateDownloadStatus(status, downloadID)

    suspend fun updateDuration(id: Int, duration: Int) =
        itemDao.updateDuration(id, duration)

    suspend fun getBookmarkAudios() = itemDao.getBookmarkAudios()
    suspend fun getBookName(downloadId: Long): BookData  = itemDao.getBookName(downloadId)
}