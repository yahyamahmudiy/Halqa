package com.example.halqa.fragment.mainflow.readbook

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.halqa.R
import com.example.halqa.activity.MainActivity
import com.example.halqa.adapter.BookTextAdapter
import com.example.halqa.databinding.FragmentReadBinding
import com.example.halqa.model.BookText
import com.google.android.material.bottomsheet.BottomSheetBehavior


class ReadFragment : Fragment(R.layout.fragment_read) {

    private val binding by viewBinding(FragmentReadBinding::bind)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var adapter: BookTextAdapter
    private var isInDarkMode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.readingSettings))
        closeAudioControlBottomSheet()
    }

    private fun closeAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun openAudioControlBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun initViews() {
        binding.apply {
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
                Log.d("TAG", "initViews: came")
                requireActivity().onBackPressed()
            }

            ivMenu.setOnClickListener {
                (requireActivity() as MainActivity).openDrawerLayout()
            }
        }

        binding.readingSettings.apply {

        }

        controlBottomSettingsViewShowHide()

        controlPageChangeWithSeekbar()

        controlTextSizeChangeWithSeekbar()

        controlOnBackPressed()

        refreshAdapter()
    }

    private fun controlOnBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Do custom work here
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                        closeAudioControlBottomSheet()
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

    private fun controlTextSizeChangeWithSeekbar() {
        binding.readingSettings.seekBarTextSize.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                currentProgress: Int,
                p2: Boolean
            ) {
                if (p2) {
                    adapter.changeFontSize(currentProgress.toFloat())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun controlPageChangeWithSeekbar() {
        binding.seekBarPage.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                currentProgress: Int,
                p2: Boolean
            ) {
                if (p2) {
                    binding.rvText.smoothScrollToPosition(currentProgress)
                    binding.tvCurrentPage.text = currentProgress.toString()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

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
            this.submitList(ArrayList<BookText>().apply {
                for (i in 0..31)
                    this.add(BookText(""))
            })
        }
        binding.rvText.adapter = adapter
    }

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
}