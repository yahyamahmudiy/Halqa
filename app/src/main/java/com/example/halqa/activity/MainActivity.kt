package com.example.halqa.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBindingWithLifecycle
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.adapter.ChapAdapter
import com.example.halqa.databinding.ActivityMainBinding
import com.example.halqa.model.Chapter

class MainActivity : AppCompatActivity() {

    lateinit var navGraph: NavGraph
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var adapter: ChapAdapter
    private val bookPageSelected by viewModels<BookPageSelectionViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }

        initViews()
        setObserver()
    }

    private fun setObserver() {
        bookPageSelected.getIsClickedFromAudioControlFr().observe(this) {
            openDrawerLayout()
        }
    }

    private fun initViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        when {
            false -> navGraph.setStartDestination(R.id.mainFlowFragment)
            !false -> navGraph.setStartDestination(R.id.languageFlowFragment)
        }

        navController.graph = navGraph

        setMenu()
    }

    fun setStartDestination(){
        navGraph.setStartDestination(R.id.mainFlowFragment)
    }

    private fun setMenu() {
        adapter = ChapAdapter()
        binding.drawerLayout.setScrimColor(resources.getColor(R.color.drawer_background_color))
        refreshAdapter()
    }

    private fun refreshAdapter() {
        adapter.submitList(ArrayList<Chapter>().apply {
            for (i in 0..20) {
                this.add(Chapter("", ""))
            }
        })

        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerView.adapter = adapter

        adapter.onChapterClick = {
            bookPageSelected.setChapterNumber(it)
            closeDrawerLayout()
        }
    }

    fun openDrawerLayout() {
        binding.drawerLayout.openDrawer(GravityCompat.END, true)
    }


    private fun closeDrawerLayout() {
        binding.drawerLayout.closeDrawer(GravityCompat.END, true)
    }
}