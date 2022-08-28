package com.example.halqa.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.halqa.R
import com.example.halqa.databinding.ItemBookmarkBinding
import com.example.halqa.manager.SharedPref
import com.example.halqa.model.BookmarkData
import com.example.halqa.utils.UiStateList

class BookmarkAdapter(context: Context) : ListAdapter<BookmarkData, BookmarkAdapter.ItemViewHolder>(ITEM_DIF) {
    var onClick: ((BookmarkData) -> Unit)? = null

    private var isBool = SharedPref(context).isSaved
    var lotin = context.getString(R.string.str_bob_lotin)
    var kirill = context.getString(R.string.str_bob_kirill)

    companion object{
        val ITEM_DIF = object : DiffUtil.ItemCallback<BookmarkData>(){
            override fun areItemsTheSame(oldItem: BookmarkData, newItem: BookmarkData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BookmarkData, newItem: BookmarkData): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ItemViewHolder(val bn: ItemBookmarkBinding): RecyclerView.ViewHolder(bn.root){
        @SuppressLint("SetTextI18n")
        fun bind(position: Int){
            val item = getItem(adapterPosition)
            with(bn){
                tvBookName.text = item.bookName

                if (isBool){
                    tvInform.text = "${item.bob}-" +  lotin
                }else{
                    tvInform.text = "${item.bob}-" + kirill
                }

                tvContinue.setOnClickListener {
                    onClick?.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    fun submitData(list: ArrayList<BookmarkData>){
        val items = ArrayList<BookmarkData>()
        items.addAll(currentList)
        items.addAll(list)
        items.reverse()
        submitList(items)
    }
}