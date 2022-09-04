package com.example.halqa.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.SplashViewModel
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookData
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.JANGCHI
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    private var progressBar: ProgressBar? = null
    private var progressStatus = 0
    private var textView: TextView? = null
    private val handler: Handler = Handler()
    var mCountDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initViews()
    }

    private fun initViews() {
        setScreen()
        setProgressBar()
    }

    fun setScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }
        val sharedPref = SharedPref(this)

        if (sharedPref.isOneCreate) {

            val halqaList = getListFromId(R.array.halqa)
            val halqaChapterNameKrillList =
                getListFromId(R.array.chapters_halqa_crill)
            val halqaChapterCommentKrillList =
                getListFromId(R.array.chapters_halqa_crill_commment)
            val halqaChapterNameLatinList =
                getListFromId(R.array.chapters_halqa_latin)
            val halqaChapterCommentLatinList =
                getListFromId(R.array.chapters_halqa_latin_comment)


            halqaList.forEachIndexed { index, item ->
                val halqa = BookData(
                    bob = "${index + 1}-bob",
                    bookName = HALQA,
                    url = item,
                    chapterNameKrill = halqaChapterNameKrillList[index],
                    chapterNameLatin = halqaChapterNameLatinList[index],
                    chapterCommentKrill = halqaChapterCommentKrillList[index],
                    chapterCommentLatin = halqaChapterCommentLatinList[index],
                    isDownload = false
                )
                viewModel.createPost(halqa)
            }
            val jangchiList = getListFromId(R.array.jangchi)
            val jangchiChapterNameKrillList =
                getListFromId(R.array.chapter_jangchi_crill)
            val jangchiChapterNameLatinList =
                getListFromId(R.array.chapters_jangchi_latin)

            jangchiList.forEachIndexed { index, item ->
                val halqa = BookData(
                    bob = "${index + 1}-bob",
                    bookName = JANGCHI,
                    url = item,
                    chapterNameKrill = jangchiChapterNameKrillList[index],
                    chapterNameLatin = jangchiChapterNameLatinList[index],
                    isDownload = false
                )
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
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun setProgressBar(){
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar!!.progressTintList =  ColorStateList.valueOf(getColor(R.color.main_color))
        // Start long running operation in a background thread
        // Start long running operation in a background thread
        Thread {
            while (progressStatus < 22) {
                progressStatus += 1
                // Update the progress bar and display the
                //current value in the text view
                handler.post(Runnable {
                    progressBar!!.progress = progressStatus
                })
                try {
                    // Sleep for 200 milliseconds.
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun getListFromId(id: Int): List<String> = resources.getStringArray(id).toList()
}