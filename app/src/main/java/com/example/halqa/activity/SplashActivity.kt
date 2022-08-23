package com.example.halqa.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.halqa.R
import com.example.halqa.manager.SharedPref

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initViews()
    }

    private fun initViews() {
        setScreen()
    }

    fun setScreen() {
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

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