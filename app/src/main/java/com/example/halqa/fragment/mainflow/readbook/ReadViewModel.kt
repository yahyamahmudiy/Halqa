package com.example.halqa.fragment.mainflow.readbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookmarkData
import com.example.halqa.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(private val repository: ItemRepository) : ViewModel() {

    /**
     * Room related
     */

    fun insertPhotoHomeDB(bookmark: BookmarkData){
        viewModelScope.launch {
            repository.insertBookmarkToDB(bookmark)
        }

    }
}