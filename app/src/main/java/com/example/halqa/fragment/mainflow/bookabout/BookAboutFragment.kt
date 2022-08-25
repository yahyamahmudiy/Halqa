package com.example.halqa.fragment.mainflow.bookabout

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.databinding.FragmentBookAboutBinding
import com.example.halqa.extension.firstCap
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookData
import com.example.halqa.model.Chapter
import com.example.halqa.receiver.AudioDownloadReceiver
import com.example.halqa.utils.Constants.BOOK
import com.example.halqa.utils.Constants.BOOK_EXTRA
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.JANGCHI
import com.example.halqa.utils.Constants.NOTSAVED
import com.example.halqa.utils.Constants.SAVED
import com.example.halqa.utils.Constants.SAVING
import com.example.halqa.utils.UiStateList
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookAboutFragment : Fragment(R.layout.fragment_book_about) {

    private val binding by viewBinding(FragmentBookAboutBinding::bind)
    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var isBool = true
    private var book: String? = null
    private val TAG = "BookAboutFragment"
    private val viewModel: BookAboutViewModel by viewModels()
    private var save: String? = null
    private lateinit var bookName: String
    private var lastDownloadID: Long = 0L
    private var downloadedAudioID: Long = 0L
    private lateinit var audioDownloadReceiver: AudioDownloadReceiver
    private var downloadList: ArrayList<Long> = ArrayList()
    private var downloadSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBool = SharedPref(requireContext()).isSaved
        arguments?.let {
            book = it.getString(BOOK)
            bookName = it.get(BOOK).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        downloadIcon()

        binding.ivDownload.setOnClickListener {
            downloadBtnClick()
        }

        initLanguageConst()

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.audioControlBottomSheet))
        disableBottomSheetDragging()
        closeAudioControlBottomSheet()

        registerDownloadBroadcast()
    }

    private fun downloadBtnClick() {
        when (save) {
            NOTSAVED -> {
                viewModel.getBookAudios(book!!)
                setUpObserver()
            }
            SAVING -> {

            }
            SAVED -> {

            }
        }

    }

    private fun downloadIcon(){
        if (book == HALQA){
            initHalqa()
            save = SharedPref(requireContext()).isSavedAudioHalqa
        }else if (book == JANGCHI){
            initJangchi()
            save = SharedPref(requireContext()).isSavedAudioJangchi
        }
        if (save == NOTSAVED){
            binding.ivDownload.setImageResource(R.drawable.ic_download_blue_icon)
        }else if (save == SAVING){
           // Glide.with(requireActivity()).load(R.drawable.loding_blue).into(binding.ivDownload)
        }else if (save == SAVED){
            binding.ivDownload.setImageResource(R.drawable.ic_play_white)
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allBookAudios.collect {
                    when (it) {
                        UiStateList.LOADING -> {
                            //show progress
                            if (book == HALQA) {
                                SharedPref(requireContext()).isSavedAudioHalqa = SAVING
                            }else{
                                SharedPref(requireContext()).isSavedAudioJangchi = SAVING
                            }
                            downloadIcon()
                        }

                        is UiStateList.SUCCESS -> {
                            Log.d("TAG", "setUpObserver: $it")
                            downloadSize = it.data.size
                            downloadList.clear()
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

    private fun initLanguageConst() {
        if (isBool){
            binding.apply {
                tvAuthor.text = requireContext().getString(R.string.str_author)
                tvRead.text = requireContext().getString(R.string.str_read)
                btnReadbook.text = requireContext().getString(R.string.str_reading_book)
                tvRateBook.text = requireContext().getString(R.string.str_rate_book)
            }
        }else{
            binding.apply {
                tvAuthor.text = requireContext().getString(R.string.str_author_kirill)
                tvRead.text = requireContext().getString(R.string.str_read_kirill)
                btnReadbook.text = requireContext().getString(R.string.str_reading_book_kirill)
                tvRateBook.text = requireContext().getString(R.string.str_rate_book_kirill)
            }
        }
    }

    private fun initHalqa() {
        binding.apply {
            ivBookImage.setImageResource(R.drawable.halqa_2)
            if (isBool) {
                tvBookName.text = requireContext().getString(R.string.str_halqa)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih)
                tvChap.text = requireContext().getString(R.string.str_32_bob_halqa)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_halqa)
            } else {
                tvBookName.text = requireContext().getString(R.string.str_halqa_kirill)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik_kirill)
                tvReadName1.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih_kirill)
                tvChap.text = requireContext().getString(R.string.str_32_bob_halqa_kirill)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_halqa_kirill)
            }
        }
    }

    private fun initJangchi() {
        binding.apply {
            ivBookImage.setImageResource(R.drawable.img_jangchi)
            if (isBool) {
                tvBookName.text = requireContext().getString(R.string.str_jangchi)
                tvAuthorName.text = requireContext().getString(R.string.str_akrom_malik)
                tvReadName1.text = requireContext().getString(R.string.str_abdukarim_mirzayev)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih)
                tvChap.text = requireContext().getString(R.string.str_14_bob_jangchi)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_jangchi)
            } else {
                tvBookName.text = requireContext().getString(R.string.str_jangchi_kirill)
                tvReadName1.text =
                    requireContext().getString(R.string.str_abdukarim_mirzayev_kirill)
                tvReadName2.text = requireContext().getString(R.string.str_shams_solih_kirill)
                tvChap.text = requireContext().getString(R.string.str_14_bob_jangchi_kirill)
                tvBookDescription.text = requireContext().getString(R.string.str_dic_jangchi_kirill)
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun disableBottomSheetDragging() {
        bottomSheetBehavior.isDraggable = false
    }

    private fun initViews() {

        setData(bookName)
        if (bookName == JANGCHI) {
            setJangchiMenu()
        } else {
            setHalqaMenu()
        }

        binding.apply {

            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            ivMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawerLayout()
            }

            btnReadbook.setOnClickListener {
                openReadFragment()
            }

            bottomAudioPlayView.setOnClickListener {
                openAudioControlBottomSheet()
            }
        }

        binding.audioControlBottomSheet.apply {
            ivBack.setOnClickListener {
                closeAudioControlBottomSheet()
            }
        }

        setPageSelectionObserver()
    }

    private fun setHalqaMenu() {
        if (SharedPref(requireContext()).getString("til") == "Lotin")
            setMenuList(
                resources.getStringArray(R.array.chapters_halqa_latin).toList()
            )
        else
            setMenuList(
                resources.getStringArray(R.array.chapters_halqa_crill).toList()
            )
    }

    private fun setMenuList(list: List<String>) {
        (requireActivity() as MainActivity).refreshAdapter(list)
    }

    private fun setJangchiMenu() {
        if (SharedPref(requireContext()).getString("til") == "Lotin")
            setMenuList(
                resources.getStringArray(R.array.chapters_jangchi_latin).toList()
            )
        else
            setMenuList(
                resources.getStringArray(R.array.chapter_jangchi_crill).toList()
            )
    }

    private fun setData(bookName: String) {
        binding.apply {
            tvBookName.text = bookName
            tvBookNameBottom.text = bookName
            audioControlBottomSheet.apply {
                tvName.text = bookName
            }
        }
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setPageSelectionObserver() {
        bookPageSelected.getChapterNumber().observe(viewLifecycleOwner) {
            setDataToBottomSheet(it)
            openAudioControlBottomSheet()
        }
    }

    private fun setDataToBottomSheet(chapter: Chapter) {
        binding.audioControlBottomSheet.apply {
            tvChapter.text = getChapterData(chapter)
        }
    }

    private fun getChapterData(chapter: Chapter): String =
        "${chapter.chapNumber + 1}-bob. ${chapter.chapName.firstCap()}"

    private fun openReadFragment() {
        findNavController().navigate(
            R.id.action_bookAboutFragment_to_readFragment,
            bundleOf(BOOK_KEY to bookName)
        )
    }

    private fun downloadFile(bookData: BookData) {
        Log.d("TAG", "setUpObserver downloadFile: $bookData")
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
        lastDownloadID = downloadID
        Log.d("TAG", "setUpObserver downloadID: $downloadID")

        audioDownloadReceiver.onDownloadCompleted = { ID ->
            downloadedAudioID = ID!!
            Log.d("TAG", "setUpObserver ID: $ID")
            downloadList.add(ID)

            if (downloadList.size == downloadSize){
                if (book == HALQA){
                    SharedPref(requireContext()).isSavedAudioHalqa = SAVED
                }else{
                    SharedPref(requireContext()).isSavedAudioJangchi = SAVED
                }
                downloadIcon()
            }
        }
    }

    private fun registerDownloadBroadcast() {
        audioDownloadReceiver = AudioDownloadReceiver()
        requireActivity().registerReceiver(
            audioDownloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }
}