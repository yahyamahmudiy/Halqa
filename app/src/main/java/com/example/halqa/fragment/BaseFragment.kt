package com.example.halqa.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

open class BaseFragment(
    @LayoutRes layout: Int,
    @IdRes private val navHostFragmentId: Int
) :
    Fragment(layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
        val navController = navHostFragment.navController

        setupNavigation(navController)
    }

    open fun setupNavigation(navController: NavController) {}
}