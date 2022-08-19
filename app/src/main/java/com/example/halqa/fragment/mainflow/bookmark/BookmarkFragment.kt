package com.example.halqa.fragment.mainflow.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentBookmarkBinding
import androidx.viewpager.widget.ViewPager;
import com.example.halqa.adapter.ViewPagerAdapter
import com.example.halqa.fragment.mainflow.bookmark.audio.AudioBMFragment
import com.example.halqa.fragment.mainflow.bookmark.books.BooksBMFragment
import com.google.android.material.tabs.TabLayout

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding by viewBinding(FragmentBookmarkBinding::bind)

    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var vpFilter: ViewPager
    private lateinit var tlFilter: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        setAdapter()
        refreshAdapter()
    }

    private fun initView() {
        tlFilter = binding.tlFilter
        vpFilter = binding.vpFilter

        refreshAdapter()

    }

    private fun setAdapter() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(BooksBMFragment())
        pagerAdapter.addFragment(AudioBMFragment())
        pagerAdapter.addTitle(getString(R.string.str_kitoblar))
        pagerAdapter.addTitle(getString(R.string.str_audio))
    }


    private fun refreshAdapter() {
        vpFilter.adapter = pagerAdapter
        tlFilter.setupWithViewPager(vpFilter)
    }
}