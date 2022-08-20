package com.example.halqa.fragment.mainflow.bookabout

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BookAboutFragment : Fragment(R.layout.fragment_book_about) {

    private val binding by viewBinding(FragmentBookAboutBinding::bind)
    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.audioControlBottomSheet))
        closeAudioControlBottomSheet()
    }

    private fun initViews() {
        binding.apply {
            ivMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawerLayout()
            }

            bottomAudioPlayView.setOnClickListener {
                openAudioControlBottomSheet()
            }
        }

        binding.audioControlBottomSheet.apply {
            ivBack.setOnClickListener {
                closeAudioControlBottomSheet()
            }
        }

        setPageSelectionObserver()
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setPageSelectionObserver() {
        bookPageSelected.getChapterNumber().observe(viewLifecycleOwner) {
            //control chapter
            Log.d("TAG", "setPageSelectionObserver: $it")
        }
    }

    private fun bottomSheetStateListener() {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }
}