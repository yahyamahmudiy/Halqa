package com.example.halqa.fragment.mainflow.saved.books

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentBooksSavedBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.utils.Constants
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.HALQA_LAST_READING_CHAPTER
import com.example.halqa.utils.Constants.JANGCHI
import com.example.halqa.utils.Constants.JANGCHI_LAST_READING_CHAPTER
import com.example.halqa.utils.Constants.KRILL
import com.example.halqa.utils.Constants.LANGUAGE
import com.example.halqa.utils.Constants.LAST_CHAPTER
import com.example.halqa.utils.Constants.LATIN
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class BooksSavedFragment : Fragment(R.layout.fragment_books_saved) {

    private val binding by viewBinding(FragmentBooksSavedBinding::bind)
    private lateinit var sharedPref: SharedPref


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        sharedPref = SharedPref(requireContext())

        binding.apply {

            if (sharedPref.getString(LANGUAGE) == KRILL)
                setKrillDataToViews()
            else setLatinDataToViews()

            cvHalqa.setOnClickListener {
                openReadFragment(sharedPref.getInt(HALQA_LAST_READING_CHAPTER), HALQA)
            }

            cvJangchi.setOnClickListener {
                openReadFragment(sharedPref.getInt(JANGCHI_LAST_READING_CHAPTER), JANGCHI)
            }
        }
    }

    private fun setLatinDataToViews() {
        binding.apply {
            tvInformJangchi.text =
                getJangchiLastChapter(JANGCHI_LAST_READING_CHAPTER, getText(R.string.str_bob_lotin))
            tvInformHalqa.text =
                getJangchiLastChapter(HALQA_LAST_READING_CHAPTER, getText(R.string.str_bob_lotin))
        }
    }

    private fun setKrillDataToViews() {
        binding.apply {
            tvJangchi.text = getText(R.string.str_jangchi_kirill)
            tvHalqa.text = getText(R.string.str_halqa_kirill)
            tvInformJangchi.text = getJangchiLastChapter(
                JANGCHI_LAST_READING_CHAPTER,
                getText(R.string.str_bob_kirill)
            )
            tvInformHalqa.text =
                getJangchiLastChapter(HALQA_LAST_READING_CHAPTER, getText(R.string.str_bob_kirill))
            tvContinueJangchi.text = getText(R.string.str_davom_ettirish_kirill)
            tvContinueHalqa.text = getText(R.string.str_davom_ettirish_kirill)
        }
    }

    private fun getJangchiLastChapter(chapter: String, text: CharSequence): String =
        if (chapter == JANGCHI_LAST_READING_CHAPTER)
            "${sharedPref.getInt(chapter) + 1}-${text}"
        else "${ceil((sharedPref.getInt(chapter) + 1).toFloat() / 2).toInt()}-${text}"


    private fun openReadFragment(lastReadingChapter: Int, bookName: String) {
        findNavController().navigate(
            R.id.action_savedFragment_to_readFragment,
            bundleOf(LAST_CHAPTER to lastReadingChapter, BOOK_KEY to bookName)
        )
    }
}