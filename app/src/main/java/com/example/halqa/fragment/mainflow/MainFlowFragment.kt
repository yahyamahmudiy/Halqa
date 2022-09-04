package com.example.halqa.fragment.mainflow

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.databinding.FragmentMainFlowBinding
import com.example.halqa.fragment.BaseFragment
import com.example.halqa.utils.hide
import com.example.halqa.utils.show
import com.example.halqa.utils.slideDown
import com.example.halqa.utils.slideUp


class MainFlowFragment : BaseFragment(R.layout.fragment_main_flow, R.id.nav_host_fragment_main) {

    private val binding by viewBinding(FragmentMainFlowBinding::bind)


    override fun setupNavigation(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.readFragment -> {
                    hideBottomNav()
                }
                else -> {
                    showBottomNav()
                }
            }
        }
    }


    private fun hideBottomNav() {
        binding.bottomNavigation.hide()
    }

    private fun showBottomNav() {
        binding.bottomNavigation.show()
    }

    private fun hideBottomNavWithAnimation() {
        binding.bottomNavigation.slideDown()
    }

    private fun showBottomNavWithAnimation() {
        binding.bottomNavigation.slideUp()
    }
}