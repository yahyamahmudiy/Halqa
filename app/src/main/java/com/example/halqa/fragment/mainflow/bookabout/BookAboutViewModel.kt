package com.example.halqa.fragment.mainflow.bookabout

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
class BookAboutViewModel @Inject
constructor(private val repository: ItemRepository): ViewModel() {

    private val _allBookAudios =
        MutableStateFlow<UiStateList<BookData>>(UiStateList.EMPTY)
    val allBookAudios = _allBookAudios
    private val _downloadId =
        MutableStateFlow<UiStateObject<Long>>(UiStateObject.EMPTY)
    val downloadId = _downloadId
    private val _updatedDownloadToTrue =
        MutableStateFlow<UiStateObject<Int>>(UiStateObject.EMPTY)
    val updatedDownloadToTrue = _updatedDownloadToTrue

    fun getBookAudios(bookName: String) = viewModelScope.launch {
        _allBookAudios.value = UiStateList.LOADING
        try {
            val response = repository.getBookAudios(bookName)
            _allBookAudios.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _allBookAudios.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun checkIsDownloadIDChange(id: Int?) = viewModelScope.launch {
        _downloadId.value = UiStateObject.LOADING
        try {
            val response = repository.getDownloadId(id!!)
            _downloadId.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            _downloadId.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun updateDownloadStatus(isDownloaded: Boolean, downloadID: Long?) = viewModelScope.launch {
        _updatedDownloadToTrue.value = UiStateObject.LOADING
        try {
            val response = repository.updateDownload(isDownloaded, downloadID)
            _updatedDownloadToTrue.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            _updatedDownloadToTrue.value =
                UiStateObject.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

}