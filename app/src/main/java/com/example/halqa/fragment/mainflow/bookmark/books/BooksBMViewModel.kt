package com.example.halqa.fragment.mainflow.bookmark.books

import androidx.lifecycle.MutableLiveData
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

    val bookmarkFromDB = MutableLiveData<ArrayList<BookmarkData>>()

    /**
     * Room related
     */

    fun getBookmarkFromDB(){
        viewModelScope.launch {
            bookmarkFromDB.postValue(repository.getBookmarkFromDB() as ArrayList<BookmarkData>)
        }
    }
}