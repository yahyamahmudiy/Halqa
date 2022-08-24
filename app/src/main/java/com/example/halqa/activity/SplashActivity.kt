package com.example.halqa.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.SplashViewModel
import com.example.halqa.db.AppDatabase
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookData
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.JANGCHI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initViews()
    }

    private fun initViews() {
        setScreen()
    }

    fun setScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }
        val sharedPref = SharedPref(this)

        if (sharedPref.isOneCreate){
            //val appDatabase = AppDatabase.getAppDBInstance(this)

            val halqaList = resources.getStringArray(R.array.halqa).toList()

            halqaList.forEachIndexed { index, item ->
                val halqa = BookData(bob = "${index + 1}-bob" , bookName = HALQA, url = item, isDownload = false)
                //appDatabase.itemDao().createPost(halqa)
                viewModel.createPost(halqa)
            }
            val jangchiList = resources.getStringArray(R.array.jangchi).toList()

            jangchiList.forEachIndexed { index, item ->
                val halqa = BookData(bob = "${index + 1}-bob" , bookName = JANGCHI, url = item, isDownload = false)
                //appDatabase.itemDao().createPost(halqa)
                viewModel.createPost(halqa)
            }

            sharedPref.isOneCreate = false
        }

        countDownTimer()
    }

    private fun countDownTimer() {
        object : CountDownTimer(4000, 1000) {
            override fun onTick(l: Long) {}
            override fun onFinish() {
                val intent = Intent(this@SplashActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }


}