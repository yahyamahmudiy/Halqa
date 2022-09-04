package com.example.halqa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.halqa.R
import com.example.halqa.databinding.ItemBookmarkAudioBinding
import com.example.halqa.databinding.ItemBookmarkBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookmarkAudioData
import com.example.halqa.model.BookmarkData
import com.example.halqa.utils.Constants.HALQA
import com.example.halqa.utils.UiStateList

class BookmarkAudioAdapter(context: Context) :
    ListAdapter<BookmarkAudioData, BookmarkAudioAdapter.ItemViewHolder>(ITEM_DIF) {

    private var isBool = SharedPref(context).isSaved
    var lotin = context.getString(R.string.str_bob_lotin)
    var kirill = context.getString(R.string.str_bob_kirill)

    lateinit var onClick: (BookmarkAudioData) -> (Unit)

    companion object {
        val ITEM_DIF = object : DiffUtil.ItemCallback<BookmarkAudioData>() {
            override fun areItemsTheSame(
                oldItem: BookmarkAudioData,
                newItem: BookmarkAudioData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: BookmarkAudioData,
                newItem: BookmarkAudioData
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ItemViewHolder(val bn: ItemBookmarkAudioBinding) :
        RecyclerView.ViewHolder(bn.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val item = getItem(adapterPosition)
            with(bn) {
                tvBookName.text = item.bookName
                if (item.bookName == HALQA) ivBook.setImageResource(R.drawable.halqa_2)
                else ivBook.setImageResource(R.drawable.img_jangchi)

                if (isBool) {
                    tvChapterNumber.text = "${item.bob}-" + lotin
                } else {
                    tvChapterNumber.text = "${item.bob}-" + kirill
                }

                tvChapterDuration.text = getDuration(item.duration / 1000)

                root.setOnClickListener {
                    onClick.invoke(currentList[position])
                }
            }
        }
    }

    private fun getDuration(duration: Int): String = String.format(
        "%02d:%02d:%02d",
        (duration / 3600),
        (duration / 60) % 60,
        duration % 60
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemBookmarkAudioBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    fun submitData(list: List<BookmarkAudioData>) {
        val items = ArrayList<BookmarkAudioData>()
        items.addAll(currentList)
        items.addAll(list)
        items.reverse()
        submitList(items)
    }
}