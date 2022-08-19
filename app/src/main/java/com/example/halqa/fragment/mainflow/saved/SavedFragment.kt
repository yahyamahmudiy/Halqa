package com.example.halqa.fragment.mainflow.saved

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.ViewPagerAdapter
import com.example.halqa.databinding.FragmentSavedBinding
import com.example.halqa.fragment.mainflow.saved.audio.AudioSVFragment
import com.example.halqa.fragment.mainflow.saved.books.BooksSVFragment
import com.google.android.material.tabs.TabLayout

class SavedFragment : Fragment(R.layout.fragment_saved) {
    private val binding by viewBinding(FragmentSavedBinding::bind)

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
        val text = binding.text

        changeIconVisible(text, vpFilter.currentItem)
        refreshAdapter()


        vpFilter.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                changeIconVisible(text, position)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


    }

    private fun changeIconVisible(view: View, position: Int) {
        view.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
    }

    private fun setAdapter() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(BooksSVFragment())
        pagerAdapter.addFragment(AudioSVFragment())
        pagerAdapter.addTitle(getString(R.string.str_kitoblar))
        pagerAdapter.addTitle(getString(R.string.str_audio))
    }


    private fun refreshAdapter() {
        vpFilter.adapter = pagerAdapter
        tlFilter.setupWithViewPager(vpFilter)
    }
}