package com.example.halqa.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HalqaBook")
data class BookData(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var bob: String = "",
    var bookName: String = "",
    var url: String = "",
    var chapterNameKrill: String = "",
    var chapterNameLatin: String = "",
    var chapterCommentKrill: String = "",
    var chapterCommentLatin: String = "",
    var isDownload: Boolean = false,
    var downloadID: Long = -1,
    var duration: Int = 0
)