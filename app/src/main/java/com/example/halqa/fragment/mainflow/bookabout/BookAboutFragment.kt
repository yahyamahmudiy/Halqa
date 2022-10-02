package com.example.halqa.fragment.mainflow.bookabout

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.example.halqa.extension.firstCap
import com.example.halqa.extension.makeVerticallyScrollable
import com.example.halqa.manager.SharedPref
import com.example.halqa.mediaplayer.AudioController
import com.example.halqa.model.BookData
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.model.Chapter
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.BOOK_EXTRA
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.DURATION
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.HALQA_AUDIO_LIST_SIZE
import com.example.halqa.utils.Constants.HALQA_LAST_LISTENING_CHAPTER
import com.example.halqa.utils.Constants.JANGCHI
import com.example.halqa.utils.Constants.JANGCHI_AUDIO_LIST_SIZE
import com.example.halqa.utils.Constants.JANGCHI_LAST_LISTENING_CHAPTER
import com.example.halqa.utils.Constants.LAST_AUDIO_CHAPTER
import com.example.halqa.utils.Constants.NOTSAVED
import com.example.halqa.utils.Constants.SAVED
import com.example.halqa.utils.Constants.SAVING
import com.example.halqa.utils.UiStateList
import com.example.halqa.utils.UiStateObject
import com.example.halqa.utils.hide
import com.example.halqa.utils.show
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class BookAboutFragment : Fragment(R.layout.fragment_book_about) {

    private val binding by viewBinding(FragmentBookAboutBinding::bind)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var isBool = true
    private var book: String? = null
    private val viewModel: BookAboutViewModel by viewModels()
    private var save: String? = null
    private var dataList = emptyList<BookData>()
    private var lastAudio = 0
    private var isFromSaved = false
    lateinit var audioController: AudioController
    private lateinit var sharedPref: SharedPref
    private lateinit var activity: MainActivity
    val chapter by lazy { Chapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
        isBool = sharedPref.isSaved
        arguments?.let {
            book = it.getString(BOOK)
            if (it.containsKey(LAST_AUDIO_CHAPTER)) {
                isFromSaved = true
                lastAudio = it.get(LAST_AUDIO_CHAPTER).toString().toInt()
                getAllBookData()
            }
        }

        activity = requireActivity() as MainActivity

        downloadIcon()

        initLanguageConst()

        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.audioControlBottomSheet))
        initViews()
        disableBottomSheetDragging()
        closeAudioControlBottomSheet()

        setUpDownloadObserver()
        setUpAudioDataObserver()

        setMenu()
    }

    private fun setMenu() {
        activity.getMenuData(book!!)
    }

    private fun initViews() {

        audioController = activity.getAudioController()

        checkIfAudioControllerWorking()

        activity.bookName = book!!

        binding.tvBookDescription.makeVerticallyScrollable()

        binding.apply {

            rbRate.isFocusable = false

            btnDownload.setOnClickListener {
                downloadBtnClick()
            }

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            ivMenu.setOnClickListener {
                activity.openDrawerLayout()
            }

            btnReadbook.setOnClickListener {
                openReadFragment()
            }

            bottomAudioPlayView.setOnClickListener {
                if (audioController.isMediaPlayerInitialized())
                    openAudioControlBottomSheet()
                else {
                    getAllBookData()
                }
            }

            ivPlayPauseBottom.setOnClickListener {
                if (!audioController.isMediaPlayerInitialized()) {
                    getAllBookData()
                    return@setOnClickListener
                }
                if (audioController.isPlaying()) {
                    changePlayPauseButton(R.drawable.ic_play_blue)
                    audioController.pauseMediaPlayer()
                } else {
                    changePlayPauseButton(R.drawable.ic_pause_blue)
                    audioController.playMediaPlayer()
                }
            }
        }

        binding.audioControlBottomSheet.apply {
            ivBack.setOnClickListener {
                closeAudioControlBottomSheet()
            }
            ivNext.setOnClickListener {
                if (lastAudio < dataList.size - 1) {
                    playAudio(++lastAudio, dataList[lastAudio].duration)
                    changePlayPauseButton(R.drawable.ic_pause_blue)
                }
            }
            ivPrevious.setOnClickListener {
                if (lastAudio > 0) {
                    playAudio(--lastAudio, dataList[lastAudio].duration)
                    changePlayPauseButton(R.drawable.ic_pause_blue)
                }
            }
            ivNext15.setOnClickListener {
                audioController.forward15Seconds()
            }
            ivPrevious15.setOnClickListener {
                audioController.backward15Seconds()
            }
            ivPlayPause.setOnClickListener {
                if (audioController.isPlaying()) {
                    audioController.pauseMediaPlayer()
                    changePlayPauseButton(R.drawable.ic_play_blue)

                } else {
                    audioController.playMediaPlayer()
                    changePlayPauseButton(R.drawable.ic_pause_blue)
                }
            }

            ivAudioBookmark.setOnClickListener {
                saveToDB()
            }

            tvSpeed.setOnClickListener {
                when (tvSpeed.text) {
                    getString(R.string.speed_1x) -> {
                        setMediaPlayerSpeed(1.5f)
                        tvSpeed.text = getString(R.string.speed_1_5x)
                    }
                    getString(R.string.speed_1_5x) -> {
                        setMediaPlayerSpeed(2f)
                        tvSpeed.text = getString(R.string.speed_2x)
                    }
                    else -> {
                        setMediaPlayerSpeed(1f)
                        tvSpeed.text = getString(R.string.speed_1x)
                    }
                }
            }
        }

        setPageSelectionObserver()

        controlOnBackPressed()

        setDataToBottomSheet()
    }

    private fun checkIfAudioControllerWorking() {
        if ((audioController.isPlaying() || audioController.isMediaPlayerInitialized())) {
            binding.bottomAudioPlayView.show()
            setDataToBottomSheet()
            updateFullDurationValue()
            setSeekBarCorrespondingly()
            setBottomSeekBarCorrespondingly()
        }

        if (audioController.isPlaying()) {
            changePlayPauseButton(R.drawable.ic_pause_blue)
        }
    }

    private fun saveToDB() {
        if (audioController.isMediaPlayerInitialized())
            viewModel.saveAudioBookmark(
                BookmarkAudioData(
                    bookName = book!!,
                    bob = lastAudio + 1,
                    duration = audioController.currentPosition()
                )
            )
        toast(getString(R.string.saved_successfully), getString(R.string.saved_successfully_krill))
    }

    private fun downloadBtnClick() {
        when (save) {
            NOTSAVED -> {
                viewModel.getBookAudios(book!!)
                setUpDownloadCompleteObserver()
            }
            SAVING -> {

            }
            SAVED -> {
                getAllBookData()
            }
        }
    }

    private fun setUpDownloadCompleteObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBooks.collect {
                    when (it) {
                        UiStateList.LOADING -> {
                            if (book == HALQA) {
                                sharedPref.isSavedAudioHalqa = SAVING
                            } else {
                                sharedPref.isSavedAudioJangchi = SAVING
                            }
                            downloadIcon()
                        }

                        is UiStateList.SUCCESS -> {
                            it.data.forEach { bookDate ->
                                downloadFile(bookDate)
                            }
                        }
                        is UiStateList.ERROR -> {
                        }
                        else -> {}
                    }
                }
            }
        }

    }

    private fun downloadIcon() {
        save = if (book == HALQA) {
            initHalqa()
            sharedPref.isSavedAudioHalqa
        } else {
            initJangchi()
            sharedPref.isSavedAudioJangchi
        }

        when (save) {
            NOTSAVED -> {
                setImage(R.drawable.ic_download_blue_icon)
                binding.progress.hide()
            }
            SAVING -> {
                setImage(0)
                binding.progress.show()
            }
            SAVED -> {
                setImage(R.drawable.ic_play_white_donwload)
                binding.progress.hide()
            }
        }
        if (getAllFilesWriteInAppStorage() == getRealAudioSize()) {
            binding.progress.hide()
            setImage(R.drawable.ic_play_white_donwload)
        }
    }

    private fun setImage(drawable: Int) {
        binding.ivDownload.setImageResource(drawable)
    }

    private fun getAllFilesWriteInAppStorage() = try {
        File("${requireActivity().getExternalFilesDir(null)}/$book$BOOK_EXTRA/$book").listFiles()!!.size
    } catch (e: Exception) {
        0
    }

    private fun getRealAudioSize() =
        if (book == HALQA) HALQA_AUDIO_LIST_SIZE else JANGCHI_AUDIO_LIST_SIZE

    private fun setUpDownloadObserver() {
        activity.onDownloadComplete = {
            binding.ivDownload.setImageResource(R.drawable.ic_play_white_donwload)
            binding.progress.hide()
        }
    }

    @SuppressLint("ResourceType")
    private fun disableBottomSheetDragging() {
        bottomSheetBehavior.isDraggable = false
    }

    private fun openReadFragment() {
        findNavController().navigate(
            R.id.action_bookAboutFragment_to_readFragment,
            bundleOf(BOOK_KEY to book)
        )
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
                context,
                folderName,
                "${bookData.bookName}${bookData.bob}.mp3"
            )
        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadID = downloadManager.enqueue(request)
        viewModel.updateAudioDownloadId(bookData.id!!, downloadID)
    }

    private fun controlOnBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here

                    if (activity.isDrawerOpen()) {
                        activity.closeDrawerLayout()
                        return
                    }

                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        closeAudioControlBottomSheet()
                        return
                    }

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )
    }

    //audio
    private fun getUri(bookData: BookData): String =
        "${bookData.bookName}${BOOK_EXTRA}/${bookData.bookName}/${bookData.bookName}${bookData.bob}.mp3"

    private fun getFilePath(audioPath: String): String =
        "${requireContext().getExternalFilesDir(null)}/$audioPath"

    private fun getDuration(durationInSecond: Int): CharSequence {
        String.format(
            "%02d:%02d:%02d",
            (durationInSecond / 3600),
            (durationInSecond / 60) % 60,
            durationInSecond % 60
        ).apply {
            return if (!this.startsWith("-"))
                this
            else "00:00:00"
        }
    }

    private fun setSeekBarCorrespondingly() {
        val handler = Handler()
        binding.audioControlBottomSheet.apply {
            seekBar.max = audioController.duration()
            tvFullDuration.text = getDuration(audioController.duration() / 1000)

            handler.postDelayed(object : Runnable {
                override fun run() {
                    try {
                        seekBar.progress =
                            audioController.currentPosition()
                        tvPassingDuration.text =
                            getDuration(audioController.currentPosition() / 1000)
                        handler.postDelayed(this, 1000)
                    } catch (e: Exception) {
                        seekBar.progress = 0
                    }
                }
            }, 0)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) audioController.seekTo(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
    }

    private fun setBottomSeekBarCorrespondingly() {
        val handler = Handler()
        binding.apply {
            seekBarBottom.max = audioController.duration()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    try {
                        seekBarBottom.progress =
                            audioController.currentPosition()
                        handler.postDelayed(this, 1000)
                    } catch (e: Exception) {
                        seekBarBottom.progress = 0
                    }
                }
            }, 0)
        }
    }

    private fun getAllBookData() {
        if (dataList.isEmpty()) {
            viewModel.getBookAudiosData(book!!)
        }
    }

    private fun playAudio(position: Int, duration: Int) {
        if (dataList[position].downloadID != -1L) {
            if (position in dataList.indices) {
                if (isBool)
                    activity.lastAudioLiveData.value = UiStateObject.SUCCESS(
                        chapter.apply {
                            bookName = book!!
                            chapNumber = lastAudio
                            chapName = dataList[position].chapterNameLatin
                        }
                    )
                else activity.lastAudioLiveData.value = UiStateObject.SUCCESS(
                    chapter.apply {
                        bookName = book!!
                        chapNumber = lastAudio
                        chapName = dataList[position].chapterNameKrill
                    }
                )
            }

            if (position in 0 until getPlayableRange()) {
                audioController.playSource(getFilePath(getUri(dataList[position])))
                updateFullDurationValue()
                changePlayPauseButton(R.drawable.ic_pause_blue)
            }
            if (position in getPlayableRange() until dataList.size) {
                lastAudio = getPlayableRange() - 1
                audioController.playSource(getFilePath(getUri(dataList[lastAudio])))
                toast(getString(R.string.alert), getString(R.string.alert_krill))
            }
            if (isFromSaved)
                if (duration > 0) audioController.seekTo(duration)

            arguments.let {
                if (it!!.containsKey(DURATION)) {
                    audioController.seekTo(it.get(DURATION).toString().toInt())
                }
            }
            saveCurrentAudioPositionToContinue()
            binding.bottomAudioPlayView.show()
        } else if (position in getPlayableRange() until dataList.size) {
            lastAudio = getPlayableRange() - 1
            audioController.playSource(getFilePath(getUri(dataList[lastAudio])))
            toast(getString(R.string.alert), getString(R.string.alert_krill))
            if (isFromSaved)
                if (duration > 0) audioController.seekTo(duration)

            arguments.let {
                if (it!!.containsKey(DURATION)) {
                    audioController.seekTo(it.get(DURATION).toString().toInt())
                }
            }
            binding.bottomAudioPlayView.show()
            saveCurrentAudioPositionToContinue()
        } else {
            toastAlert()
        }
    }

    private fun toastAlert() {
        changePlayPauseButton(R.drawable.ic_play_blue)
        if (isBool)
            Toast.makeText(
                requireContext(),
                getString(R.string.str_notdownload),
                Toast.LENGTH_SHORT
            ).show()
        else
            Toast.makeText(
                requireContext(),
                getString(R.string.str_notdownload_krill),
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun changePlayPauseButton(drawable: Int) {
        try {
            binding.apply {
                ivPlayPauseBottom.setImageResource(drawable)
                audioControlBottomSheet.ivPlayPause.setImageResource(drawable)
            }
        } catch (e: Exception) {
        }
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setPageSelectionObserver() {
        activity.onChapterSelected = {
            if (dataList.isNotEmpty())
                playAudio(it.chapNumber, dataList[lastAudio].duration)


            lastAudio = it.chapNumber
            getAllBookData()
            openAudioControlBottomSheet()
        }
    }

    private fun setUpAudioDataObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBooksAudio.collect {
                    when (it) {
                        UiStateList.LOADING -> {}

                        is UiStateList.SUCCESS -> {
                            playBook(it.data)
                            viewModel.allBooksAudio.value = UiStateList.LOADING
                        }
                        is UiStateList.ERROR -> {}
                        else -> {}
                    }
                }
            }
        }
    }

    private fun playBook(data: List<BookData>) {
        //initializing data list
        if (dataList.isEmpty()) dataList = data
        openAudioControlBottomSheet()
        playAudio(lastAudio, dataList[lastAudio].duration)
        setSeekBarCorrespondingly()
        setBottomSeekBarCorrespondingly()
        changePlayPauseButton(R.drawable.ic_pause_blue)
    }

    private fun setDataToBottomSheet() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activity.lastAudioLiveData.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiStateObject.SUCCESS -> {
                            try {
                                binding.tvBookNameBottom.text = getPlayingBookName(it.data.bookName)
                                binding.audioControlBottomSheet.tvName.text =
                                    getPlayingBookName(it.data.bookName)
                                binding.audioControlBottomSheet.tvChapter.text =
                                    getChapterData(it.data.chapNumber, it.data.chapName)
                            } catch (e: Exception) {
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun getPlayingBookName(bookName: String): String {
        return if (!isBool) {
            if (bookName == JANGCHI) getString(R.string.str_jangchi_kirill)
            else getString(R.string.str_halqa_kirill)
        } else bookName
    }

    private fun getChapterData(chapterNumber: Int, chapterName: String): String =
        "${chapterNumber + 1}-bob. ${chapterName.firstCap()}"

    private fun toast(string1: String, string2: String) {
        if (isBool) Toast.makeText(requireContext(), string1, Toast.LENGTH_LONG)
            .show()
        else Toast.makeText(requireContext(), string2, Toast.LENGTH_LONG)
            .show()
    }

    private fun getPlayableRange(): Int =
        if (book == HALQA) HALQA_AUDIO_LIST_SIZE else JANGCHI_AUDIO_LIST_SIZE

    private fun updateFullDurationValue() {
        try {
            binding.audioControlBottomSheet.tvFullDuration.text =
                getDuration(audioController.duration() / 1000)
            binding.audioControlBottomSheet.seekBar.max = audioController.duration()
            binding.seekBarBottom.max = audioController.duration()
        } catch (e: Exception) {
            //no problem
        }
    }

    private fun saveCurrentAudioPositionToContinue() {
        if (dataList.isNotEmpty()) {
            try {
                activity.bookData = dataList[lastAudio]
                sharedPref.saveInt(getChapterKey(), lastAudio)
            } catch (e: Exception) {
            }
        }
    }

    private fun getChapterKey(): String =
        if (book == HALQA) HALQA_LAST_LISTENING_CHAPTER else JANGCHI_LAST_LISTENING_CHAPTER

    private fun setMediaPlayerSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            audioController.getMediaPlayer().playbackParams =
                audioController.getMediaPlayer().playbackParams.setSpeed(speed)
        }
    }

    private fun initLanguageConst() {
        if (isBool) {
            binding.apply {
                tvAuthor.text = getString(R.string.str_author)
                tvAuthorName.text = getString(R.string.str_akrom_malik)
                tvRead.text = getString(R.string.str_read)
                btnReadbook.text = getString(R.string.str_reading_book)
                tvRateBook.text = getString(R.string.str_rate_book)
            }
        } else {
            binding.apply {
                tvAuthor.text = getString(R.string.str_author_kirill)
                tvAuthorName.text = getString(R.string.str_akrom_malik_kirill)
                tvRead.text = getString(R.string.str_read_kirill)
                btnReadbook.text = getString(R.string.str_reading_book_kirill)
                tvRateBook.text = getString(R.string.str_rate_book_kirill)
            }
        }
    }

    private fun initHalqa() {
        binding.apply {
            ivBookImage.setImageResource(R.drawable.halqa_2)
            ivBookBottomMain.setImageResource(R.drawable.halqa_2)
            ivBookBackground.setImageResource(R.drawable.halqa_2)
            audioControlBottomSheet.ivBook.setImageResource(R.drawable.halqa_2)
            if (isBool) {
                tvBookName.text = getString(R.string.str_halqa)
                tvReadName1.text = getString(R.string.str_abdukarim_mirzayev)
                audioControlBottomSheet.tvAudioSpeaker.text =
                    getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = getString(R.string.str_shams_solih)
                tvChap.text = getString(R.string.str_32_bob_halqa)
                tvBookDescription.text = getString(R.string.str_dic_halqa)
                tvAudioSpeaker.text = getString(R.string.str_abdukarim_mirzayev)
                tvAudioDuration.text = getString(R.string.str_halqa_duration_latin)
                tvAudioSize.text = getString(R.string.str_halqa_audio_size_latin)
            } else {
                tvBookName.text = getString(R.string.str_halqa_kirill)
                tvReadName1.text =
                    getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = getString(R.string.str_shams_solih_kirill)
                tvChap.text = getString(R.string.str_32_bob_halqa_kirill)
                tvBookDescription.text = getString(R.string.str_dic_halqa_kirill)
                tvAudioSpeaker.text = getString(R.string.str_abdukarim_mirzayev_kirill)
                audioControlBottomSheet.tvAudioSpeaker.text =
                    getString(R.string.str_abdukarim_mirzayev_kirill)
                tvAudioDuration.text = getString(R.string.str_halqa_duration_krill)
                tvAudioSize.text = getString(R.string.str_halqa_audio_size_krill)
            }
        }
    }

    private fun initJangchi() {
        binding.apply {
            audioControlBottomSheet.ivBook.setImageResource(R.drawable.img_jangchi)
            ivBookImage.setImageResource(R.drawable.img_jangchi)
            ivBookBottomMain.setImageResource(R.drawable.img_jangchi)
            ivBookBackground.setImageResource(R.drawable.img_jangchi)
            if (isBool) {
                audioControlBottomSheet.tvAudioSpeaker.text =
                    getString(R.string.str_abdukarim_mirzayev)
                tvBookName.text = getString(R.string.str_jangchi)
                tvReadName1.text = getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = getString(R.string.str_shams_solih)
                tvChap.text = getString(R.string.str_14_bob_jangchi)
                tvBookDescription.text = getString(R.string.str_dic_jangchi)
                tvAudioSpeaker.text = getString(R.string.str_abdukarim_mirzayev)
                tvAudioDuration.text = getString(R.string.str_jangchi_duration_latin)
                tvAudioSize.text = getString(R.string.str_jangchi_audio_size_latin)
            } else {
                audioControlBottomSheet.tvAudioSpeaker.text =
                    getString(R.string.str_abdukarim_mirzayev_kirill)
                tvBookName.text = getString(R.string.str_jangchi_kirill)
                tvReadName1.text =
                    getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = getString(R.string.str_shams_solih_kirill)
                tvChap.text = getString(R.string.str_14_bob_jangchi_kirill)
                tvBookDescription.text = getString(R.string.str_dic_jangchi_kirill)
                tvAudioSpeaker.text = getString(R.string.str_abdukarim_mirzayev_kirill)
                tvAudioDuration.text = getString(R.string.str_jangchi_duration_krill)
                tvAudioSize.text = getString(R.string.str_jangchi_audio_size_krill)
            }
        }
    }

}