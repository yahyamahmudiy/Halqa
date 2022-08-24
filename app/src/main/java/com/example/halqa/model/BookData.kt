package com.example.halqa.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HalqaBook")
data class BookData(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var bob: String,
    var bookName: String,
    var url: String,
    var isDownload: Boolean = false,
    var isPlaying: Boolean = false,
    var downloadID: Long = -1
)