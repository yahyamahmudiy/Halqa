package com.example.halqa.fragment.mainflow.bookmark.books

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentBooksBmBinding
import com.example.halqa.manager.SharedPref

class BooksBMFragment : Fragment(R.layout.fragment_books_bm) {
    private val binding by viewBinding(FragmentBooksBmBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.tvDavomEttirish.setOnClickListener {
            val bundle = Bundle()
            var page = SharedPref(requireContext()).getString("page")
            bundle.putString("page", page)
            findNavController().navigate(R.id.action_booksFragmentBM_to_readFragment, bundle)
        }
    }

}