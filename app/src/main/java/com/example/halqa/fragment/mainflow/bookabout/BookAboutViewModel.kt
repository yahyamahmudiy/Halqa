package com.example.halqa.fragment.mainflow.bookabout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.repository.ItemRepository
import com.example.halqa.utils.UiStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookAboutViewModel @Inject
constructor(private val repository: ItemRepository) : ViewModel() {

    private val _allBooks =
        MutableStateFlow<UiStateList<BookData>>(UiStateList.EMPTY)
    val allBooks = _allBooks


    private val _allBooksAudio =
        MutableStateFlow<UiStateList<BookData>>(UiStateList.EMPTY)
    var allBooksAudio = _allBooksAudio

    fun getBookAudios(bookName: String) = viewModelScope.launch {
        _allBooks.value = UiStateList.LOADING
        try {
            val response = repository.getBookAudiosNotEmptyUrl(bookName)
            _allBooks.value = UiStateList.SUCCESS(response)
            _allBooks.value = UiStateList.LOADING
        } catch (e: Exception) {
            _allBooks.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun getBookAudiosData(bookName: String) = viewModelScope.launch {
        _allBooksAudio.value = UiStateList.LOADING
        try {
            val response = repository.getBookAudios(bookName)
            _allBooksAudio.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _allBooksAudio.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }

    fun updateAudioDownloadId(id: Int, downloadID: Long) = viewModelScope.launch {
        try {
            repository.updateDownloadId(id, downloadID)
        } catch (e: Exception) {
        }
    }

    fun saveAudioBookmark(audioData: BookmarkAudioData) = viewModelScope.launch {
        try {
            repository.insertAudioBookmarkToDB(audioData)
        } catch (e: Exception) {
        }
    }
}

