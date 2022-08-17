package com.example.halqa.fragment.mainflow.bookabout

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.halqa.R
import com.example.halqa.adapter.ChapAdapter
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.example.halqa.model.Chap

class BookAboutFragment : Fragment(R.layout.fragment_book_about) {
    private lateinit var binding: FragmentBookAboutBinding
    private lateinit var adapter: ChapAdapter
    private lateinit var chapList: ArrayList<Chap>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookAboutBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        adapter = ChapAdapter()
        chapList = ArrayList()

        binding.ivMenu.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.END, true)
        }

        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))
        chapList.add(Chap("", ""))

        adapter.submitList(chapList)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerView.adapter = adapter
    }

}