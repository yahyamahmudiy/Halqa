package com.example.halqa.repository

import com.example.halqa.db.ItemDao
import com.example.halqa.model.BookData
import com.example.halqa.model.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    /**
     *Room db Related
     */

    suspend fun createPost(bookData: BookData) = itemDao.createPost(bookData)
    suspend fun getBookAudios(bookName: String) = itemDao.getBookAudios(bookName)
}