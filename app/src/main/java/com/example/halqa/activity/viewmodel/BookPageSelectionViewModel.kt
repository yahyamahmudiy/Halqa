package com.example.halqa.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookPageSelectionViewModel : ViewModel() {

    private val timeState = MutableLiveData<Int>()

    fun setChapterNumber(page: Int) {
        timeState.value = page
    }

    fun getChapterNumber(): LiveData<Int> {
        return timeState
    }
}