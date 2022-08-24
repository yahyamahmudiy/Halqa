package com.example.halqa.model

data class Chapter(
    val chapNumber: String,
    val chapName: String,
    val chapComment: String? = null,
    var isAudioClick:Boolean = false
)
