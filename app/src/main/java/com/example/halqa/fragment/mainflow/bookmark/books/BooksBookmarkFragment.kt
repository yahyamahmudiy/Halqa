package com.example.halqa.fragment.mainflow.bookmark.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.BookmarkAdapter
import com.example.halqa.databinding.FragmentBooksBookmarkBinding
import com.example.halqa.model.BookmarkData
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class BooksBookmarkFragment : Fragment(R.layout.fragment_books_bookmark) {

    private val binding by viewBinding(FragmentBooksBookmarkBinding::bind)
    private val viewModel: BooksBMViewModel by viewModels()
    val adapter by lazy { BookmarkAdapter(requireContext()) }
    private val TAG = "BooksBMFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getBookmarkFromDB()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkFromDB()
    }

    private fun initObserve() {
        viewModel.bookmarkFromDB.observe(viewLifecycleOwner) {
            refreshAdapter(it)
        }
    }

    private fun refreshAdapter(it: ArrayList<BookmarkData>) {
        adapter.submitData(it)
        binding.rvBookmarkBook.adapter = adapter
    }

}