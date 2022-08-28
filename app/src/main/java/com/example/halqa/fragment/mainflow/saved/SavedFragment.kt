package com.example.halqa.fragment.mainflow.saved

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.adapter.ViewPagerAdapter
import com.example.halqa.databinding.FragmentSavedBinding
import com.example.halqa.fragment.mainflow.bookmark.audio.AudioBookmarkFragment
import com.example.halqa.fragment.mainflow.bookmark.books.BooksBookmarkFragment
import com.example.halqa.fragment.mainflow.saved.audio.AudioSavedFragment
import com.example.halqa.fragment.mainflow.saved.books.BooksSavedFragment
import com.example.halqa.manager.SharedPref
import com.google.android.material.tabs.TabLayout

class SavedFragment : Fragment(R.layout.fragment_saved) {
    private val binding by viewBinding(FragmentSavedBinding::bind)

    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var vpFilter: ViewPager
    private lateinit var tlFilter: TabLayout
    private var isBool = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBool = SharedPref(requireContext()).isSaved
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
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


        if (isBool){
            binding.tvIsNotSaved.text = requireContext().getString(R.string.str_saqlangan_kitoblar_bo_limi_xozircha_bo_sh)
            binding.text.text = requireActivity().getString(R.string.str_saqlab_qo_yilganlar)
        }else{
            binding.tvIsNotSaved.text = requireContext().getString(R.string.str_saqlangan_kitoblar_bo_limi_xozircha_bo_sh_kirill)
            binding.text.text = requireActivity().getString(R.string.str_saqlab_qo_yilganlar_kirill)
        }

    }

    private fun changeIconVisible(view: View, position: Int) {
        view.visibility = if (position == 0) View.VISIBLE else View.INVISIBLE
    }

    private fun setAdapter() {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(BooksSavedFragment())
        pagerAdapter.addFragment(AudioSavedFragment())
        if (isBool){
            pagerAdapter.addTitle(getString(R.string.str_kitoblar))
            pagerAdapter.addTitle(getString(R.string.str_audio))
        }else{
            pagerAdapter.addTitle(getString(R.string.str_kitoblar_kirill))
            pagerAdapter.addTitle(getString(R.string.str_audio_kirill))
        }
    }


    private fun refreshAdapter() {
        vpFilter.adapter = pagerAdapter
        tlFilter.setupWithViewPager(vpFilter)
    }
}