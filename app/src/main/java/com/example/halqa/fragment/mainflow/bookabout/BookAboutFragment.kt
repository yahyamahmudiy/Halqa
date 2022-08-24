package com.example.halqa.fragment.mainflow.bookabout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.example.halqa.extension.firstCap
import com.example.halqa.helper.SharePref
import com.example.halqa.model.Chapter
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.JANGCHI
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BookAboutFragment : Fragment(R.layout.fragment_book_about) {

    private val binding by viewBinding(FragmentBookAboutBinding::bind)
    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var isBool = true
    private var book: String? = null
    private val TAG = "BookAboutFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBool = SharePref(requireContext()).isSaved
        arguments?.let {
            book = it.getString(BOOK)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (book == HALQA){
            initHalqa()
        }else if (book == JANGCHI){
            initJangchi()
        }

        initLanguageConst()

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.audioControlBottomSheet))
        disableBottomSheetDragging()
        closeAudioControlBottomSheet()
    }

    private fun initLanguageConst() {
        if (isBool){
            binding.apply {
                tvAuthor.text = requireContext().getString(R.string.str_author)
                tvRead.text = requireContext().getString(R.string.str_read)
                btnReadbook.text = requireContext().getString(R.string.str_reading_book)
                tvRateBook.text = requireContext().getString(R.string.str_rate_book)
            }
        }else{
            binding.apply {
                tvAuthor.text = requireContext().getString(R.string.str_author_kirill)
                tvRead.text = requireContext().getString(R.string.str_read_kirill)
                btnReadbook.text = requireContext().getString(R.string.str_reading_book_kirill)
                tvRateBook.text = requireContext().getString(R.string.str_rate_book_kirill)
            }
        }
    }

    private fun initHalqa() {
        binding.apply {
            ivBookImage.setImageResource(R.drawable.halqa_2)
            if (isBool) {
                tvBookName.text = requireContext().getString(R.string.str_halqa)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih)
                tvChap.text = requireContext().getString(R.string.str_32_bob_halqa)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_halqa)
            }else{
                tvBookName.text = requireContext().getString(R.string.str_halqa_kirill)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik_kirill)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih_kirill)
                tvChap.text = requireContext().getString(R.string.str_32_bob_halqa_kirill)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_halqa_kirill)
            }
        }
    }

    private fun initJangchi() {
        binding.apply {
            ivBookImage.setImageResource(R.drawable.img_jangchi)
            if (isBool) {
                tvBookName.text = requireContext().getString(R.string.str_jangchi)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih)
                tvChap.text = requireContext().getString(R.string.str_14_bob_jangchi)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_jangchi)
            }else{
                tvBookName.text = requireContext().getString(R.string.str_jangchi_kirill)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih_kirill)
                tvChap.text = requireContext().getString(R.string.str_14_bob_jangchi_kirill)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_jangchi_kirill)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun disableBottomSheetDragging() {
        bottomSheetBehavior.isDraggable = false
    }

    private fun initViews() {

        binding.apply {

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            ivMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawerLayout()

                if (isBool == true && book == HALQA){
                    (requireActivity() as MainActivity).refreshAdapter(requireActivity().resources.getStringArray(R.array.bob_halqa_lotin), requireActivity().resources.getStringArray(R.array.bob_halqa_comment_lotin))
                }else if (isBool == false && book == HALQA){
                    (requireActivity() as MainActivity).refreshAdapter(requireActivity().resources.getStringArray(R.array.bob_halqa_kirill), requireActivity().resources.getStringArray(R.array.bob_halqa_comment_kirill))
                }else if (isBool == true && book == JANGCHI){
                    (requireActivity() as MainActivity).refreshAdapter(requireActivity().resources.getStringArray(R.array.bob_jangchi_lotin), null)
                }else if (isBool == false && book == JANGCHI){
                    (requireActivity() as MainActivity).refreshAdapter(requireActivity().resources.getStringArray(R.array.bob_jangchi_kirill), null)
                }
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