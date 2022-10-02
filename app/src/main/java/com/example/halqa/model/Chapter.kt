package com.example.halqa.model

data class Chapter(
    var bookName:String = "",
    var chapNumber: Int = 0,
    var chapName: String = "",
    var chapterComment: String = "",
    var isDownloaded:Boolean = false
)
