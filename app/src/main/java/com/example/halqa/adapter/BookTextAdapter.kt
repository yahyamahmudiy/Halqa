package com.example.halqa.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.halqa.databinding.ItemBookChapViewBinding
import com.example.halqa.databinding.TextLayoutBinding
import com.example.halqa.model.BookText

class BookTextAdapter : ListAdapter<String, BookTextAdapter.VH>(DiffUtil()) {

    private var fontSize: Float = 12f

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: TextLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(TextLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.d("TAG", "onBindViewHolder: ${currentList[position]}")
        holder.binding.apply {
            tvText.textSize = fontSize
            tvText.text = currentList[position]
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeFontSize(size: Float) {
        this.fontSize = size
        notifyDataSetChanged()
    }
}