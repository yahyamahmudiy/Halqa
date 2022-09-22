package com.example.halqa.activity.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookData
import com.example.halqa.repository.ItemRepository
import com.example.halqa.utils.UiStateList
import com.example.halqa.utils.UiStateObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject
constructor(private val repository: ItemRepository) : ViewModel() {

    private val _allBookData =
        MutableStateFlow<UiStateList<BookData>>(UiStateList.EMPTY)
    val allBookData = _allBookData

    private val _updateStatus =
        MutableStateFlow<UiStateObject<Int>>(UiStateObject.EMPTY)
    val updateStatus = _updateStatus

    private val _downloadedSize =
        MutableStateFlow<UiStateObject<Int>>(UiStateObject.EMPTY)
    val downloadedSize = _downloadedSize

    private val _bookName =
        MutableStateFlow<UiStateObject<BookData>>(UiStateObject.EMPTY)
    val bookName = _bookName

    fun getDownloadedBookDataSize(bookName: String, isDownloaded: Boolean) = viewModelScope.launch {
        _downloadedSize.value = UiStateObject.LOADING
        try {
            val response = repository.getDownloadedBookDataSize(bookName, isDownloaded)
            _downloadedSize.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            _downloadedSize.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }


    fun updateDownloadStatus(status: Boolean, downloadID: Long) = viewModelScope.launch {
        _updateStatus.value = UiStateObject.LOADING
        try {
            val response = repository.updateDownloadStatus(status, downloadID)
            _updateStatus.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            _updateStatus.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }


    fun getBookAudios(bookName: String) = viewModelScope.launch {
        _allBookData.value = UiStateList.LOADING
        try {
            val response = repository.getBookAudios(bookName)
            _allBookData.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _allBookData.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun getBookName(downloadId: Long) = viewModelScope.launch {
        _bookName.value = UiStateObject.LOADING
        try {
            val response = repository.getBookName(downloadId)
            _bookName.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            _bookName.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun updateAudioDownloadId(id: Int, downloadID: Long) = viewModelScope.launch {
        try {
            repository.updateDownloadId(id, downloadID)
        } catch (e: Exception) {
        }
    }

    fun saveLastDuration(id: Int, duration: Int) = viewModelScope.launch {
        try {
            repository.updateDuration(id, duration)
        } catch (e: Exception) { }
    }
}