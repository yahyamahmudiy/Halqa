package com.example.halqa.fragment.mainflow.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.databinding.FragmentMainBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.JANGCHI

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private var isBool = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBool = SharedPref(requireContext()).isSaved
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        language()
        initViews()
    }

    private fun language() {
        if (isBool) {
            binding.apply {
                tvBio.text = requireContext().getString(R.string.str_biografiya)
                tvAkromMalikMain.text = requireContext().getString(R.string.str_akrom_malik)
                tvAbdukarimMirzayev.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvKitoblar.text = requireContext().getString(R.string.str_kitoblar)
                tvBookName.text = requireContext().getString(R.string.str_halqa)
                tvInform.text = requireContext().getString(R.string.str_akrom_malik)
                tvBob.text = requireContext().getString(R.string.str_32_bob_halqa)
                tvJangchi.text = requireContext().getString(R.string.str_jangchi)
                tvAkromMalikIkki.text = requireContext().getString(R.string.str_akrom_malik)
                tvAbdukarimMirzayevIkki.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvBob2.text = requireContext().getString(R.string.str_14_bob_jangchi)
            }
        } else {
            binding.apply {
                tvBio.text = requireContext().getString(R.string.str_biografiya_kirill)
                tvAkromMalikMain.text = requireContext().getString(R.string.str_akrom_malik_kirill)
                tvAbdukarimMirzayev.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvKitoblar.text = requireContext().getString(R.string.str_kitoblar_kirill)
                tvBookName.text = requireContext().getString(R.string.str_halqa_kirill)
                tvInform.text = requireContext().getString(R.string.str_akrom_malik_kirill)
                tvBob.text = requireContext().getString(R.string.str_32_bob_halqa_kirill)
                tvJangchi.text = requireContext().getString(R.string.str_jangchi_kirill)
                tvAkromMalikIkki.text = requireContext().getString(R.string.str_akrom_malik_kirill)
                tvAbdukarimMirzayevIkki.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvBob2.text = requireContext().getString(R.string.str_14_bob_jangchi_kirill)
            }
        }
    }

    private fun initViews() {
        binding.apply {
            cvHalqa.setOnClickListener {
                initMenu(HALQA)
                findNavController().navigate(
                    R.id.action_mainFragment_to_bookAboutFragment,
                    bundleOf(BOOK to HALQA)
                )
            }

            cvJangchi.setOnClickListener {
                initMenu(JANGCHI)
                findNavController().navigate(
                    R.id.action_mainFragment_to_bookAboutFragment,
                    bundleOf(BOOK to JANGCHI)
                )
            }
        }
    }

    private fun initMenu(bookName: String) {
        (requireActivity() as MainActivity).getMenuData(bookName)
    }
}