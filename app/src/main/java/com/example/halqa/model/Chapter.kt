package com.example.halqa.model

data class Chapter(
    val chapNumber: String,
    val chapName: String,
    val chapComment: String? = null
    var chapNumber: Int = 0,
    var chapName: String = "",
    var isAudioClick:Boolean = false
)
