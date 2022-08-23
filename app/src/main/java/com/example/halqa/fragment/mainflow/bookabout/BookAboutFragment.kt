package com.example.halqa.fragment.mainflow.bookabout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.constants.Constants.BOOK_KEY
import com.example.halqa.constants.Constants.HALQA
import com.example.halqa.constants.Constants.JANGCHI
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.example.halqa.extension.firstCap
import com.example.halqa.extension.setImage
import com.example.halqa.model.Chapter
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BookAboutFragment : Fragment(R.layout.fragment_book_about) {

    private val binding by viewBinding(FragmentBookAboutBinding::bind)
    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private fun setMenuList(list: List<String>) {
        (requireActivity() as MainActivity).submitList(list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.audioControlBottomSheet))
        disableBottomSheetDragging()
        closeAudioControlBottomSheet()
    }

    private fun disableBottomSheetDragging() {
        bottomSheetBehavior.isDraggable = false
    }

    private fun initViews() {

        arguments.let {
            setData(it?.get(BOOK_KEY).toString())
            if (it?.get(BOOK_KEY).toString() == JANGCHI) {
                setMenuList(
                    resources.getStringArray(R.array.chapters_jangchi_latin).toList()
                )
            } else {
                setMenuList(
                    resources.getStringArray(R.array.chapters_halqa_latin).toList()
                )
            }
        }

        binding.apply {

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

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

    private fun setData(bookName: String) {
        binding.apply {
            tvBookName.text = bookName
            tvBookNameBottom.text = bookName
            audioControlBottomSheet.apply {
                tvName.text = bookName
            }
            if (bookName == HALQA) {
                setBookData("32-bob", R.drawable.halqa_2)

            } else {
                setBookData("14-bob", R.drawable.img_jangchi)
            }
        }
    }

    private fun setBookData(bobNumber: String, drawable: Int) {
        binding.apply {
            tvChapterNumber.text = bobNumber
            ivBookMain.setImage(drawable)
            ivBookBottomMain.setImage(drawable)
            ivBookBackground.setImage(drawable)
            audioControlBottomSheet.ivBook.setImage(drawable)
        }
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setPageSelectionObserver() {
        bookPageSelected.getChapterNumber().observe(viewLifecycleOwner) {
            if (it.isAudioClick) {
                setDataToBottomSheet(it)
                openAudioControlBottomSheet()
            } else {
                openReadFragment()
            }
        }
    }

    private fun setDataToBottomSheet(chapter: Chapter) {
        binding.audioControlBottomSheet.apply {
            tvChapter.text = getChapterData(chapter)
        }
    }

    private fun getChapterData(chapter: Chapter): String =
        "${chapter.chapNumber + 1}-bob. ${chapter.chapName.firstCap()}"


    private fun openReadFragment() {
        findNavController().navigate(R.id.action_bookAboutFragment_to_readFragment)
    }
}