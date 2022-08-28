package com.example.halqa.fragment.mainflow.saved.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentBooksSavedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksSavedFragment : Fragment(R.layout.fragment_books_saved) {
    private val binding by viewBinding(FragmentBooksSavedBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

    }
}