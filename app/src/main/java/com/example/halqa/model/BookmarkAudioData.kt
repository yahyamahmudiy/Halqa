package com.example.halqa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_audio_data")
data class BookmarkAudioData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    @ColumnInfo(name = "bookName") var bookName: String = "",
    @ColumnInfo(name = "bob") var bob: Int = 0,
    @ColumnInfo(name = "duration") var duration: Int = 0
)