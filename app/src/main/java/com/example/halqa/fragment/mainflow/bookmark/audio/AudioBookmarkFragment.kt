package com.example.halqa.fragment.mainflow.bookmark.audio

import android.os.Bundle
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
import com.example.halqa.adapter.BookmarkAudioAdapter
import com.example.halqa.databinding.FragmentAudioBookmarkBinding
import com.example.halqa.fragment.mainflow.bookabout.BookAboutViewModel
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.utils.Constants
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.DURATION
import com.example.halqa.utils.Constants.LAST_AUDIO_CHAPTER
import com.example.halqa.utils.UiStateList
import com.example.halqa.utils.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AudioBookmarkFragment : Fragment(R.layout.fragment_audio_bookmark) {

    private val binding by viewBinding(FragmentAudioBookmarkBinding::bind)
    private val viewModel: BookmarkAudioViewModel by viewModels()
    val adapter by lazy { BookmarkAudioAdapter(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBookmarkAudios()
        initViews()
    }

    private fun initViews() {
        setUpObserver()
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allAudioBookmark.collect {
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

    private fun refreshAdapter(data: List<BookmarkAudioData>) {
        adapter.submitData(data)
        binding.rvBookmarkAudio.adapter = adapter

        adapter.onClick = {
            findNavController().navigate(
                R.id.action_bookmarkFragment_to_bookAboutFragment,
                bundleOf(
                    BOOK to it.bookName,
                    LAST_AUDIO_CHAPTER to it.bob - 1,
                    DURATION to it.duration
                )
            )
        }
    }
}