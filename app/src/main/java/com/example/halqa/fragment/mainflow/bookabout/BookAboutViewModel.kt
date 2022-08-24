package com.example.halqa.fragment.mainflow.bookabout

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
class BookAboutViewModel @Inject
constructor(private val repository: ItemRepository): ViewModel() {

    private val _allBookAudios =
        MutableStateFlow<UiStateList<BookData>>(UiStateList.EMPTY)
    val allBookAudios = _allBookAudios

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

}