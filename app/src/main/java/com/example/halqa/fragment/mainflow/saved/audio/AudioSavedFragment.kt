package com.example.halqa.fragment.mainflow.saved.audio

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentAudioSavedBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.utils.Constants
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.HALQA_LAST_LISTENING_CHAPTER
import com.example.halqa.utils.Constants.JANGCHI
import com.example.halqa.utils.Constants.JANGCHI_LAST_LISTENING_CHAPTER
import com.example.halqa.utils.Constants.LAST_AUDIO_CHAPTER
import kotlin.math.ceil

class AudioSavedFragment : Fragment(R.layout.fragment_audio_saved) {

    private val binding by viewBinding(FragmentAudioSavedBinding::bind)
    private lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
        initViews()
    }

    private fun initViews() {
        binding.apply {

            if (sharedPref.getString(Constants.LANGUAGE) == Constants.KRILL)
                setKrillDataToViews()
            else setLatinDataToViews()


            cvJangchi.setOnClickListener {
                openBookAboutFragment(
                    sharedPref.getInt(
                        JANGCHI_LAST_LISTENING_CHAPTER
                    ), JANGCHI
                )
            }

            cvHalqa.setOnClickListener {
                openBookAboutFragment(
                    sharedPref.getInt(
                        HALQA_LAST_LISTENING_CHAPTER
                    ), HALQA
                )
            }
        }
    }

    private fun setLatinDataToViews() {
        binding.apply {
            tvChapterJangchi.text =
                getJangchiLastChapter(
                    Constants.JANGCHI_LAST_LISTENING_CHAPTER,
                    getText(R.string.str_bob_lotin)
                )
            tvChapterHalqa.text =
                getJangchiLastChapter(
                    Constants.HALQA_LAST_LISTENING_CHAPTER,
                    getText(R.string.str_bob_lotin)
                )
        }
    }

    private fun setKrillDataToViews() {
        binding.apply {
            tvBookJangchi.text = getText(R.string.str_jangchi_kirill)
            tvBookHalqa.text = getText(R.string.str_halqa_kirill)
            tvChapterJangchi.text = getJangchiLastChapter(
                JANGCHI_LAST_LISTENING_CHAPTER,
                getText(R.string.str_bob_kirill)
            )
            tvChapterHalqa.text =
                getJangchiLastChapter(
                    HALQA_LAST_LISTENING_CHAPTER,
                    getText(R.string.str_bob_kirill)
                )
            tvContinueJangchi.text = getText(R.string.str_davom_ettirish_kirill)
            tvContinueHalqa.text = getText(R.string.str_davom_ettirish_kirill)
        }
    }

    private fun openBookAboutFragment(chapterPosition: Int, book: String) {
        findNavController().navigate(
            R.id.action_savedFragment_to_bookAboutFragment,
            bundleOf(LAST_AUDIO_CHAPTER to chapterPosition, BOOK to book)
        )
    }

    private fun getJangchiLastChapter(chapter: String, text: CharSequence): String =
        if (chapter == JANGCHI_LAST_LISTENING_CHAPTER)
            "${sharedPref.getInt(chapter) + 1}-${text}"
        else "${sharedPref.getInt(chapter) + 1}-${text}"

}