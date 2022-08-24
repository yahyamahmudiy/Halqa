package com.example.halqa.fragment.languageflow.language

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.databinding.FragmentLanguageBinding
import com.example.halqa.helper.SharePref
import com.example.halqa.manager.SharedPref

class LanguageFragment : Fragment(R.layout.fragment_language) {
    private val binding by viewBinding(FragmentLanguageBinding::bind)
    private var isBool = true
    private var language = "Lotin"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        selectLanguage()
    }

    fun selectLanguage() {
        binding.apply {
            btnLotin.setOnClickListener {
                language = "Lotin"
                btnLotin.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_blue)
                btnLotin.setTextColor(resources.getColor(R.color.white))

                btnKirill.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_white)
                btnKirill.setTextColor(resources.getColor(R.color.black))

                btnContinue.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_blue)
                btnContinue.setTextColor(resources.getColor(R.color.white))
                btnContinue.isClickable = true
                isBool = true
            }

            btnKirill.setOnClickListener {
                language = "Krill"
                btnKirill.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_blue)
                btnKirill.setTextColor(resources.getColor(R.color.white))

                btnLotin.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_white)
                btnLotin.setTextColor(resources.getColor(R.color.black))

                btnContinue.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_blue)
                btnContinue.setTextColor(resources.getColor(R.color.white))
                btnContinue.isClickable = true
                isBool = false
            }

            btnContinue.setOnClickListener {
                (requireActivity() as MainActivity).setStartDestination()
                SharePref(requireContext()).isSaved = isBool
                SharedPref(requireContext()).saveString("til", language)
            }
        }
    }
}