package com.example.halqa.fragment.mainflow.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentLanguageBinding
import com.example.halqa.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            cvHalqa.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_bookAboutFragment)
            }

            cvJangchi.setOnClickListener {

            }
        }
    }

}