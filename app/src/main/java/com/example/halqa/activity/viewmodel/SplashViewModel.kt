package com.example.halqa.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.halqa.model.BookData
import com.example.halqa.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject
constructor(private val repository: ItemRepository): ViewModel() {

    fun createPost(bookData: BookData){
        viewModelScope.launch {
            repository.createPost(bookData)
        }
    }

}