package com.example.halqa.fragment.mainflow.bookmark.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.BookmarkAdapter
import com.example.halqa.databinding.FragmentBooksBmBinding
import com.example.halqa.utils.UiStateList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksBMFragment : Fragment(R.layout.fragment_books_bm) {
    private val binding by viewBinding(FragmentBooksBmBinding::bind)

    private val viewModel: BooksBMViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    val adapter by lazy { BookmarkAdapter(requireContext()) }
    private val TAG = "BooksBMFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBookmarkFromDB()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBookmarkFromDB()
    }

    private fun initView() {
        initObserve()
        recyclerView = binding.rvBookmark
        val lm = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = lm
        recyclerView.adapter = adapter

        adapter.onClick = {item->
            val bundle = Bundle()
            bundle.putString("page", item.bob)
            findNavController().navigate(R.id.action_booksFragmentBM_to_readFragment, bundle)
        }
    }

    private fun initObserve() {
        viewModel.bookmarkFromDB.observe(viewLifecycleOwner){
            adapter.submitData(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){

        }

        viewModel.errorMessage.observe(viewLifecycleOwner){

        }
    }

}