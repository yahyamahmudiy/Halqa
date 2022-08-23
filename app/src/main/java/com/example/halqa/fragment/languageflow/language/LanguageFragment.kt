package com.example.halqa.fragment.languageflow.language

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.databinding.FragmentLanguageBinding
import com.example.halqa.manager.SharedPref

class LanguageFragment : Fragment(R.layout.fragment_language) {
    private val binding by viewBinding(FragmentLanguageBinding::bind)
    lateinit var language:String

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

                language = "Lotin"
            }

            btnKirill.setOnClickListener {
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

                language = "Kirill"
            }

            btnContinue.setOnClickListener {
                SharedPref(requireContext()).saveBoolean("introDone",true)
                SharedPref(requireContext()).saveString("til",language)
                (requireActivity() as MainActivity).setStartDestination()
            }
        }
    }
}