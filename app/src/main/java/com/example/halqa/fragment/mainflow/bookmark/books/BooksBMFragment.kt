package com.example.halqa.fragment.mainflow.bookmark.books

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.BookmarkAdapter
import com.example.halqa.databinding.FragmentBooksBmBinding
import com.example.halqa.manager.SharedPref
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksBMFragment : Fragment(R.layout.fragment_books_bm) {
    private val binding by viewBinding(FragmentBooksBmBinding::bind)

    private val viewModel: BooksBMViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    val adapter by lazy { BookmarkAdapter(requireContext()) }
    private val TAG = "BooksBMFragment"
    private var isBool = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBool = SharedPref(requireContext()).isSaved
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initObserve()
        viewModel.getBookmarkFromDB()
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
            if (it.isEmpty()){
                binding.isEmpty.visibility = View.VISIBLE

                if (isBool){
                    binding.tvText.text = requireContext().getString(R.string.str_text)
                }else{
                    binding.tvText.text = requireContext().getString(R.string.str_text_kirill)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){

        }

        viewModel.errorMessage.observe(viewLifecycleOwner){

        }
    }
}