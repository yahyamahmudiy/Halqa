package com.example.halqa.fragment.mainflow.readbook

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.activity.viewmodel.BookPageSelectionViewModel
import com.example.halqa.adapter.BookTextAdapter
import com.example.halqa.databinding.FragmentReadBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookmarkData
import com.example.halqa.utils.Constants.BOOK_KEY
import com.example.halqa.utils.Constants.FONT_SIZE
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.Constants.HALQA_LAST_READING_CHAPTER
import com.example.halqa.utils.Constants.JANGCHI
import com.example.halqa.utils.Constants.JANGCHI_LAST_READING_CHAPTER
import com.example.halqa.utils.Constants.LAST_CHAPTER
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class ReadFragment : Fragment(R.layout.fragment_read) {

    private val viewModel: ReadViewModel by viewModels()
    private val binding by viewBinding(FragmentReadBinding::bind)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val bookPageSelected by activityViewModels<BookPageSelectionViewModel>()
    private lateinit var adapter: BookTextAdapter
    private var isInDarkMode = false
    private var page: Int? = null
    private lateinit var bookName: String

    lateinit var sharedPref: SharedPref


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            bookName = it.get(BOOK_KEY).toString()
            if (it.containsKey(LAST_CHAPTER))
                page = it.get(LAST_CHAPTER).toString().toInt()
        }

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.readingSettings))
        closeAudioControlBottomSheet()
    }

    override fun onDetach() {
        super.onDetach()
        saveToDB()
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun initViews() {
        sharedPref = SharedPref(requireContext())
        binding.apply {
            if (isCurrentBookHalqa()) {
                seekBarPage.max = 33
                tvFullPages.text = "33"
            } else {
                seekBarPage.max = 16
                tvFullPages.text = "16"
            }

            btnSettings.setOnClickListener {
                openAudioControlBottomSheet()
            }
            btnDarkLight.setOnClickListener {
                if (!isInDarkMode)
                    changeModeToDark()
                else
                    changeModeToLight()
                isInDarkMode = !isInDarkMode
            }

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            ivMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawerLayout()
            }
        }

        setMenu()

        controlBottomSettingsViewShowHide()

        controlScreenBrightnessWithSeekbar()

        controlPageChangeWithSeekbar()

        controlTextSizeChangeWithSeekbar()

        controlRecyclerViewScroll()

        controlOnBackPressed()

        controlBookmark()

        refreshAdapter()

        setUpBookPageSelectionObserver()
    }

    private fun setMenu() {
        if (isCurrentBookHalqa())
            setHalqaMenu()
        else
            setJangchiMenu()
    }

    private fun setUpBookPageSelectionObserver() {
        bookPageSelected.getChapterNumber().observe(viewLifecycleOwner) {
            if (isCurrentBookHalqa())
                scrollToPosition(it.chapNumber * 2)
            else scrollToPosition(it.chapNumber)
        }
    }

    private fun scrollToPosition(position: Int) {
        (binding.rvText.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
            position,
            100
        )
    }

    private fun controlBookmark() {
        binding.btnBookmark.setOnClickListener {
            saveToDB()
        }
        if (page != null) {
            scrollToPosition(page!!)
            binding.tvCurrentPage.text = page.toString()
        }

    }

    private fun saveToDB() {
        val page = binding.tvCurrentPage.text.toString()
        val bookName = binding.tvBookName.text.toString()
        viewModel.insertPhotoHomeDB(BookmarkData(0, bookName, page))
    }

    private fun controlRecyclerViewScroll() {
        binding.rvText.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.seekBarPage.progress = lastPosition()
                binding.tvCurrentPage.text =
                    if (isCurrentBookHalqa())
                        getHalqaLastVisibleItemPosition().toString()
                    else getJangchiLastVisibleItemPosition().toString()
            }
        })
    }

    private fun controlOnBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                        closeAudioControlBottomSheet()
                        return
                    }
                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })
    }

    private fun controlTextSizeChangeWithSeekbar() {
        binding.readingSettings.seekBarTextSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                currentProgress: Int,
                p2: Boolean,
            ) {
                if (p2) {
                    adapter.changeFontSize(currentProgress.toFloat())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
                sharedPref.saveInt(FONT_SIZE, p0!!.progress)
            }
        })
    }

    private fun controlPageChangeWithSeekbar() {
        binding.seekBarPage.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                currentProgress: Int,
                p2: Boolean,
            ) {
                if (p2) {
                    if (isCurrentBookHalqa())
                        scrollToPosition(currentProgress * 2)
                    else scrollToPosition(currentProgress)
                    if (currentProgress == 0) binding.tvCurrentPage.text = "1"
                    else binding.tvCurrentPage.text = currentProgress.toString()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun isCurrentBookHalqa(): Boolean = bookName == HALQA

    private fun controlBottomSettingsViewShowHide() {
        binding.rvText.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var y: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                y = dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (y <= 0)
                            binding.settings.animate().translationY((0).toFloat()).interpolator =
                                AccelerateInterpolator(1F)
                        else {
                            y = 0
                            binding.settings.animate()
                                .translationY(binding.settings.height.toFloat())
                                .setInterpolator(AccelerateInterpolator(2F)).start()
                        }
                    }

                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        if (y >= 0)
                            binding.settings.animate()
                                .translationY(binding.settings.height.toFloat())
                                .setInterpolator(AccelerateInterpolator(2F)).start()
                    }
                }
            }
        })
    }

    private fun refreshAdapter() {
        adapter = BookTextAdapter().apply {
            this.submitList(resources.getStringArray(getNeededArray()).toList())
        }
        sharedPref.getInt(FONT_SIZE).apply {
            binding.readingSettings.seekBarTextSize.progress
            adapter.changeFontSizeWithoutDataSet(this.toFloat())
        }
        binding.rvText.adapter = adapter
    }

    private fun getNeededArray(): Int = if (bookName == JANGCHI) {
        if (sharedPref.getString("til") == "Lotin")
            R.array.text_of_chapters_jangchi_latin
        else R.array.text_of_chapters_jangchi_crill
    } else if (sharedPref.getString("til") == "Lotin")
        R.array.text_of_chapters_halqa_latin
    else R.array.text_of_chapters_halqa_crill

    private fun changeModeToDark() {
        binding.apply {
            root.setBackgroundColor(resources.getColor(R.color.dark_mode_background))
            settings.setBackgroundColor(resources.getColor(R.color.dark_mode_background))
            btnDarkLight.setColorFilter(resources.getColor(R.color.main_color))
            tvBookName.setTextColor(resources.getColor(R.color.white))
            tvCurrentPage.setTextColor(resources.getColor(R.color.white))
            tvFullPages.setTextColor(resources.getColor(R.color.white))
            ivBack.setColorFilter(resources.getColor(R.color.white))
            ivMenu.setColorFilter(resources.getColor(R.color.white))
            seekBarPage.thumb.setTint(resources.getColor(R.color.white))
            seekBarPage.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.dark_mode_seekbar_track))
            seekBarPage.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        }
    }

    private fun changeModeToLight() {
        binding.apply {
            root.background = resources.getDrawable(R.drawable.main_gradient)
            settings.background = resources.getDrawable(R.drawable.main_gradient)
            btnDarkLight.setColorFilter(resources.getColor(R.color.light_main_color))
            tvBookName.setTextColor(resources.getColor(R.color.dark_text_book_name_color))
            tvCurrentPage.setTextColor(resources.getColor(R.color.dark_text_seekbar_color))
            tvFullPages.setTextColor(resources.getColor(R.color.dark_text_seekbar_color))
            ivBack.setColorFilter(resources.getColor(R.color.dark_text_book_name_color))
            ivMenu.setColorFilter(resources.getColor(R.color.dark_text_book_name_color))
            seekBarPage.thumb.setTint(resources.getColor(R.color.main_color))
            seekBarPage.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.main_color))
            seekBarPage.progressTintList =
                ColorStateList.valueOf(resources.getColor(R.color.dark_mode_light_blue))
        }
    }

    private fun controlScreenBrightnessWithSeekbar() {
        binding.readingSettings.seekBarBrightness.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                currentProgress: Int,
                p2: Boolean
            ) {
                changeScreenBrightness(currentProgress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun changeScreenBrightness(level: Float) {
        val layoutParams: WindowManager.LayoutParams =
            requireActivity().window.attributes
        layoutParams.screenBrightness = level
        requireActivity().window.attributes = layoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isCurrentBookHalqa()) sharedPref.saveInt(
            HALQA_LAST_READING_CHAPTER,
            lastPosition()
        )
        else sharedPref.saveInt(
            JANGCHI_LAST_READING_CHAPTER, lastPosition()
        )
    }

    private fun getHalqaLastVisibleItemPosition(): Int =
        ceil((lastPosition() + 1).toFloat() / 2).toInt()

    private fun getJangchiLastVisibleItemPosition(): Int = lastPosition() + 1

    private fun lastPosition(): Int =
        (binding.rvText.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

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
}