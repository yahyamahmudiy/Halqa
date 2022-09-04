package com.example.halqa.fragment.mainflow.bookmark.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookmarkData
import com.example.halqa.repository.ItemRepository
import com.example.halqa.utils.UiStateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksBMViewModel @Inject constructor(private val repository: ItemRepository) : ViewModel() {

    private val _bookmarkFromDB =
        MutableStateFlow<UiStateList<BookmarkData>>(UiStateList.EMPTY)
    val bookmarkFromDB = _bookmarkFromDB

    /**
     * Room related
     */

    fun getBookmarkFromDB() = viewModelScope.launch {
        _bookmarkFromDB.value = UiStateList.LOADING
        try {
            val response = repository.getBookmarkFromDB()
            _bookmarkFromDB.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _bookmarkFromDB.value =
                UiStateList.ERROR(e.localizedMessage ?: "No connection", false)
        }
    }
}