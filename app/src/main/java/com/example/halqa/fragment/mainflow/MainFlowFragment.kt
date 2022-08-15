package com.example.halqa.fragment.mainflow

import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.databinding.FragmentMainFlowBinding
import com.example.halqa.fragment.BaseFragment


class MainFlowFragment : BaseFragment(R.layout.fragment_main_flow, R.id.nav_host_fragment_main) {

    private val binding by viewBinding(FragmentMainFlowBinding::bind)

    override fun setupNavigation(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)
    }
}