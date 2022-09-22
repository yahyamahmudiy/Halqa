package com.example.halqa.activity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.halqa.R
import com.example.halqa.activity.viewmodel.MainActivityViewModel
import com.example.halqa.adapter.ChapAdapter
import com.example.halqa.databinding.ActivityMainBinding
import com.example.halqa.dialog.DownloadSuccessDialog
import com.example.halqa.manager.SharedPref
import com.example.halqa.mediaplayer.AudioController
import com.example.halqa.model.BookData
import com.example.halqa.model.Chapter
import com.example.halqa.utils.Constants
import com.example.halqa.utils.Constants.BOOK_EXTRA
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.HALQA_AUDIO_LIST_SIZE
import com.example.halqa.utils.Constants.JANGCHI_AUDIO_LIST_SIZE
import com.example.halqa.utils.Constants.LANGUAGE
import com.example.halqa.utils.Constants.LATIN
import com.example.halqa.utils.UiStateList
import com.example.halqa.utils.UiStateObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var onDownloadComplete: () -> Unit
    lateinit var navGraph: NavGraph
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var adapter: ChapAdapter
    private val viewModel by viewModels<MainActivityViewModel>()
    private val chapterList: ArrayList<Chapter> = arrayListOf()
    var bookName: String = ""
    private var downloadId: Long = 0
    private lateinit var sharedPref: SharedPref
    private var bookList: List<BookData>? = null
    private lateinit var audioController: AudioController
    lateinit var onChapterSelected: (Chapter) -> Unit
    var bookData = BookData()
    val lastAudioLiveData =
        MutableLiveData<UiStateObject<Chapter>>(UiStateObject.EMPTY)
    private var handler = Handler(Looper.getMainLooper())
    private var isOnBackground = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }

        registerReceiver(
            broadcastReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        initViews()
    }

    override fun onStart() {
        super.onStart()
        isOnBackground = false
    }

    private fun initViews() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        checkSaved()

        navController.graph = navGraph

        binding.ivDownloadAll.setOnClickListener {
            bookList?.forEach {
                downloadFile(it)
            }
            if (bookName == HALQA) {
                sharedPref.isSavedAudioHalqa = Constants.SAVING
            } else {
                sharedPref.isSavedAudioJangchi = Constants.SAVING
            }
        }

        audioController = AudioController(this).getInstance()

        setMenu()

        initObservers()
    }

    private fun downloadFile(bookData: BookData) {
        val folderName = "${bookData.bookName}${BOOK_EXTRA}/${bookData.bookName}"
        val request = DownloadManager.Request(Uri.parse(bookData.url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(bookData.bookName)
            .setDescription("${bookData.bookName} audio kitob ${bookData.bob}")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalFilesDir(
                this,
                folderName,
                "${bookData.bookName}${bookData.bob}.mp3"
            )
        val downloadManager =
            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)
        viewModel.updateAudioDownloadId(bookData.id!!, downloadID)
    }

    private fun initObservers() {
        setUpDownloadStatusUpdateObserver()
        setUpBookNameObserver()
        setUpDownloadedSizeObserver()
    }

    fun setStartDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        navGraph.setStartDestination(R.id.mainFlowFragment)

        navController.graph = navGraph
    }

    private fun checkSaved() {
        if (SharedPref(this).getString(LANGUAGE)!!.isNotBlank()) {
            navGraph.setStartDestination(R.id.mainFlowFragment)
        } else {
            navGraph.setStartDestination(R.id.languageFlowFragment)
        }
    }

    private fun setMenu() {
        adapter = ChapAdapter()
        initObserver()
        binding.drawerLayout.setScrimColor(resources.getColor(R.color.drawer_background_color))
    }

    fun getMenuData(bookName: String) {
        viewModel.getBookAudios(bookName)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBookData.collect {
                    when (it) {
                        UiStateList.LOADING -> {}
                        is UiStateList.SUCCESS -> {
                            refreshMenuAdapter(it.data)
                            bookList = it.data
                        }
                        is UiStateList.ERROR -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    private fun refreshMenuAdapter(list: List<BookData>) {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)

        adapter.submitList(getChapterList(list))

        binding.recyclerView.adapter = adapter

        adapter.onChapterClick = {
            //bookPageSelected.setChapterNumber(it)
            onChapterSelected.invoke(it)
            closeDrawerLayout()
        }
    }

    private fun getChapterList(allBookData: List<BookData>): List<Chapter> {
        chapterList.clear()
        if (SharedPref(this).getString(LANGUAGE) == LATIN)
            allBookData.forEach {
                chapterList.add(
                    Chapter(
                        getBobNumber(it.bob),
                        it.chapterNameLatin,
                        it.chapterCommentLatin,
                        it.isDownload
                    )
                )
            }
        else allBookData.forEach {
            chapterList.add(
                Chapter(
                    getBobNumber(it.bob),
                    it.chapterNameKrill,
                    it.chapterCommentKrill,
                    it.isDownload
                )
            )
        }
        return chapterList
    }

    private fun getBobNumber(bob: String): Int = bob.substring(0, bob.indexOf("-")).toInt() - 1

    fun openDrawerLayout() {
        binding.drawerLayout.openDrawer(GravityCompat.END, true)
    }

    fun closeDrawerLayout() {
        binding.drawerLayout.closeDrawer(GravityCompat.END, true)
    }

    fun isDrawerOpen(): Boolean = binding.drawerLayout.isDrawerVisible(GravityCompat.END)

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            viewModel.updateDownloadStatus(
                true,
                downloadId
            )
        }
    }

    private fun setUpDownloadStatusUpdateObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateStatus.collect {
                    when (it) {
                        UiStateObject.LOADING -> {}

                        is UiStateObject.SUCCESS -> {
                            viewModel.getBookName(downloadId)
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpBookNameObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookName.collect {
                    when (it) {
                        UiStateObject.LOADING -> {}

                        is UiStateObject.SUCCESS -> {
                            bookName = it.data.bookName
                            viewModel.getDownloadedBookDataSize(bookName, true)
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpDownloadedSizeObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.downloadedSize.collect {
                    when (it) {
                        UiStateObject.LOADING -> {}

                        is UiStateObject.SUCCESS -> {
                            if (it.data == getAllFilesWriteInAppStorage() && getAllFilesWriteInAppStorage() == getRealAudioSize()) {
                                showDownloadedDialog()
                                saveToSharedPrefSaved()
                                onDownloadComplete.invoke()
                                getMenuData(bookName)
                            }
                        }
                        is UiStateObject.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun getAllFilesWriteInAppStorage() =
        File("${getExternalFilesDir(null)}/$bookName$BOOK_EXTRA/$bookName").listFiles()!!.size

    private fun getRealAudioSize() =
        if (bookName == HALQA) HALQA_AUDIO_LIST_SIZE else JANGCHI_AUDIO_LIST_SIZE

    private fun showDownloadedDialog() {
        DownloadSuccessDialog(this).show()
    }

    private fun saveToSharedPrefSaved() {
        if (bookName == HALQA) {
            sharedPref.isSavedAudioHalqa = Constants.SAVED
        } else {
            sharedPref.isSavedAudioJangchi = Constants.SAVED
        }
    }

    fun getAudioController() = audioController

    private fun saveCurrentAudioPositionToContinue(duration: Int) {
        try {
            if (bookData.id != null)
                handler.post(object : Runnable {
                    override fun run() {
                        if (isOnBackground) {
                            viewModel.saveLastDuration(bookData.id!!, duration)
                        }
                        handler.postDelayed(this, 10000)
                    }
                })
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
        isOnBackground = true
        saveCurrentAudioPositionToContinue(getPosition())
    }

    private fun getPosition(): Int =
        if (audioController.currentPosition() > 0) audioController.currentPosition() else 0
}
