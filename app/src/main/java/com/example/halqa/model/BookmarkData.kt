package com.example.halqa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_table", indices = [Index(value = ["bob"], unique = true)])
data class BookmarkData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "bookName") var bookName: String? = null,
    @ColumnInfo(name = "bob") var bob: String? = null,
)
