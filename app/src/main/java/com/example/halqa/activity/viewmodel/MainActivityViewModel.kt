package com.example.halqa.activity.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookData
import com.example.halqa.repository.ItemRepository
import com.example.halqa.utils.UiStateList
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
}