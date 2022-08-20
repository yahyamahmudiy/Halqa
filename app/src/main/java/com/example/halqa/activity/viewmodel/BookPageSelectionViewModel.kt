package com.example.halqa.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookPageSelectionViewModel : ViewModel() {

    private val timeState = MutableLiveData<Int>()
    private val isClicked = MutableLiveData<Boolean>()

    fun setChapterNumber(page: Int) {
        timeState.value = page
    }

    fun getChapterNumber(): LiveData<Int> {
        return timeState
    }

    fun getIsClickedFromAudioControlFr(): LiveData<Boolean> {
        return isClicked
    }

    fun setIsClickedFromAudioControlFr(isClicked: Boolean) {
        this.isClicked.value = isClicked
    }
}