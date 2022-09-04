package com.example.halqa.fragment.mainflow.bookmark.audio

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
class BookmarkAudioViewModel @Inject constructor(val itemRepository: ItemRepository) : ViewModel() {

    private val _allAudioBookmark =
        MutableStateFlow<UiStateList<BookmarkAudioData>>(UiStateList.EMPTY)
    val allAudioBookmark = _allAudioBookmark

    fun getBookmarkAudios() = viewModelScope.launch {
        _allAudioBookmark.value = UiStateList.LOADING
        try {
            val response = itemRepository.getBookmarkAudios()
            _allAudioBookmark.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _allAudioBookmark.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }
}