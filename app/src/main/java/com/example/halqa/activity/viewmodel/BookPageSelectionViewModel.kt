package com.example.halqa.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.halqa.model.BookData
import com.example.halqa.model.Chapter

class BookPageSelectionViewModel : ViewModel() {

    private val timeState = MutableLiveData<Chapter>()

    fun setChapterNumber(chapter: Chapter) {
        timeState.value = chapter
    }

    fun getChapterNumber(): LiveData<Chapter> {
        return timeState
    }
}