package com.example.halqa.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.model.BookmarkData

@Dao
interface ItemDao {

    @Query("SELECT * FROM bookmark_table")
    suspend fun getBookmarkFromDB(): List<BookmarkData>

    @Query("SELECT * FROM bookmark_audio_data")
    suspend fun getBookmarkAudios(): List<BookmarkAudioData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmarkToDB(bookmark: BookmarkData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioBookmarkToDB(bookmarkAudioData: BookmarkAudioData)

    @Query("DELETE FROM bookmark_table ")
    suspend fun deleteBookmarkFromDB()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPost(halqa: BookData)

    @Query("SELECT * FROM halqabook WHERE bookName = :bookName")
    suspend fun getBookAudios(bookName: String): List<BookData>

    @Query("SELECT * FROM halqabook WHERE bookName = :bookName AND NULLIF(url, '') IS NOT NULL ")
    suspend fun getBookAudiosNotEmptyUrl(bookName: String): List<BookData>

    @Query("UPDATE halqabook SET downloadID=:downloadID WHERE id=:id")
    suspend fun updateDownloadId(id: Int, downloadID: Long)

    @Query("UPDATE halqabook SET isDownload=:isDownload WHERE downloadID=:ID")
    suspend fun updateDownloadStatus(isDownload: Boolean, ID: Long): Int

    @Query("SELECT COUNT(bookName) FROM halqabook WHERE bookName =:bookName AND isDownload=:isDownloaded ")
    suspend fun getDownloadedBookDataSize(bookName: String, isDownloaded: Boolean): Int

    @Query("UPDATE halqabook SET duration=:duration WHERE id=:id")
    suspend fun updateDuration(id: Int, duration: Int): Int

    @Query("SELECT * FROM halqabook WHERE downloadID=:downloadId")
    suspend fun getBookName(downloadId: Long): BookData

}
