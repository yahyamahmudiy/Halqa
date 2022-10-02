package com.example.halqa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.halqa.R
import com.example.halqa.databinding.ItemBookChapViewBinding
import com.example.halqa.model.Chapter

class ChapAdapter : ListAdapter<Chapter, RecyclerView.ViewHolder>(DiffUtil()) {

    lateinit var onChapterClick: ((Chapter) -> Unit)

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
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
                    onChapterClick.invoke(item)
                }
                holder.view.apply {
                    if (item.isDownloaded) ivPlay.setImageResource(R.drawable.ic_play)
                    else ivPlay.setImageResource(R.drawable.ic_play_grey)
                    if (currentList.size == 33) {
                        if (position != 32)
                            tvChapNumber.text = "${position + 1}-bob"
                        else tvChapNumber.text = ""
                        tvChapName.text = item.chapName
                        tvChapComment.text = item.chapterComment
                    } else {
                        tvChapNumber.text = ""
                        tvChapName.text = item.chapName
                        tvChapComment.text = ""
                    }
                }
            }
        }
    }
}
