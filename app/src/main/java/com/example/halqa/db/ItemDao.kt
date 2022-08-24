package com.example.halqa.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.halqa.model.BookData
import com.example.halqa.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_table")
    suspend fun getFromDB(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDB(pin: Item)

    @Query("DELETE FROM item_table ")
    suspend fun deleteFromDB()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPost(halqa: BookData)

    @Query("SELECT * FROM halqabook WHERE bookName = :bookName")
    suspend fun getBookAudios(bookName: String): List<BookData>

}
