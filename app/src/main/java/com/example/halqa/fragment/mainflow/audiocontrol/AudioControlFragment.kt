package com.example.halqa.fragment.mainflow.audiocontrol

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.databinding.AudioControlScreenBinding

class AudioControlFragment : Fragment(R.layout.audio_control_screen) {

    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var binding: AudioControlScreenBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AudioControlScreenBinding.bind(view)

        initViews()
        setPageSelectionObserver()
    }

    private fun setPageSelectionObserver() {
        bookPageSelected.getChapterNumber().observe(viewLifecycleOwner) {
            Log.d("TAG", "setPageSelectionObserver: $it")
        }
    }

    private fun initViews() {
        binding.apply {
            ivMenu.setOnClickListener {
                bookPageSelected.setIsClickedFromAudioControlFr(true)
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}