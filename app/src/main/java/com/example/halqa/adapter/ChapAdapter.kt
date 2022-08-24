package com.example.halqa.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.halqa.databinding.ItemBookChapViewBinding
import com.example.halqa.model.Chapter

class ChapAdapter : ListAdapter<String, RecyclerView.ViewHolder>(DiffUtil()) {

    lateinit var onChapterClick: ((Chapter) -> Unit)
    private val chapter = Chapter()

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    sealed class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        class ItemBookChapView(val view: ItemBookChapViewBinding) :
            ViewHolder(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            ItemBookChapViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder.ItemBookChapView(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolder.ItemBookChapView -> {
                holder.view.root.setOnClickListener {
                    onChapterClick.invoke(chapter.apply {
                        chapNumber = position
                        chapName = getChapterName(item)
                        isAudioClick = false
                    })
                }

                holder.view.ivPlay.setOnClickListener {
                    onChapterClick.invoke(chapter.apply {
                        chapNumber = position
                        chapName = getChapterName(item)
                        isAudioClick = true
                    })
                }

                holder.view.apply {
                    if (currentList.size == 33) {
                        if (position != 32)
                            tvChapNumber.text = "${position + 1}-bob"
                        else tvChapNumber.text = ""
                        tvChapName.text = getChapterName(item)
                        tvChapComment.text = getChapterTeller(item)
                    } else {
                        tvChapNumber.text = ""
                        tvChapName.text = item
                        tvChapComment.text = ""
                    }
                }
            }
        }
    }

    private fun getChapterTeller(item: String): CharSequence =
        item.subSequence(item.indexOf("{") + 1, item.length - 1)

    private fun getChapterName(item: String): String =
        try {
            if (currentList.size == 33) item.substring(0, item.indexOf("{")) else item
        } catch (e: Exception) {
            Log.d("TAG", "getChapterName: $item")
            ""
        }
}