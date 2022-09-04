package com.example.halqa.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.halqa.model.Chapter
import com.example.halqa.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow

class BookPageSelectionViewModel : ViewModel() {

    private val timeState = MutableStateFlow<UiStateObject<Chapter>>(UiStateObject.EMPTY)

    fun setChapterNumber(chapter: Chapter) {
        timeState.value = UiStateObject.SUCCESS(chapter)
    }

    fun getChapterNumber(): MutableStateFlow<UiStateObject<Chapter>> {
        return timeState
    }

    fun setLoading() {
        timeState.value = UiStateObject.EMPTY
    }
}