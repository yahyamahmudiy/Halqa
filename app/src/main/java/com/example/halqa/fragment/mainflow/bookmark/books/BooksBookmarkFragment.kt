package com.example.halqa.fragment.mainflow.bookmark.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.BookmarkAdapter
import com.example.halqa.databinding.FragmentBooksBookmarkBinding
import com.example.halqa.model.BookmarkData
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.LAST_CHAPTER
import com.example.halqa.utils.UiStateList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksBookmarkFragment : Fragment(R.layout.fragment_books_bookmark) {

    private val binding by viewBinding(FragmentBooksBookmarkBinding::bind)
    private val viewModel: BooksBMViewModel by viewModels()
    val adapter by lazy { BookmarkAdapter(requireContext()) }

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookmarkFromDB.collect {
                    when (it) {
                        UiStateList.LOADING -> {}

                        is UiStateList.SUCCESS -> {
                            refreshAdapter(it.data)
                        }
                        is UiStateList.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun refreshAdapter(it: List<BookmarkData>) {
        adapter.submitData(it)
        binding.rvBookmarkBook.adapter = adapter

        adapter.onClick = {
            Log.d("TAG", "refreshAdapter: $it")
            findNavController().navigate(
                R.id.action_bookmarkFragment_to_readFragment,
                bundleOf(LAST_CHAPTER to it.position, BOOK_KEY to it.bookName)
            )
        }
    }

}