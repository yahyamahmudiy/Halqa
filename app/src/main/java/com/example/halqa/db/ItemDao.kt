package com.example.halqa.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkData

@Dao
interface ItemDao {
    @Query("SELECT * FROM bookmark_table")
    suspend fun getBookmarkFromDB(): List<BookmarkData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkToDB(bookmark: BookmarkData)

    @Query("DELETE FROM bookmark_table ")
    suspend fun deleteBookmarkFromDB()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPost(halqa: BookData)

    @Query("SELECT * FROM halqabook WHERE bookName = :bookName")
    suspend fun getBookAudios(bookName: String): List<BookData>

}
