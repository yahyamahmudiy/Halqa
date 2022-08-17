package com.example.halqa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.halqa.databinding.ItemBookChapViewBinding
import com.example.halqa.model.Chap

class ChapAdapter: ListAdapter<Chap, RecyclerView.ViewHolder>(DiffUtil()) {

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Chap>() {
        override fun areItemsTheSame(oldItem: Chap, newItem: Chap): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chap, newItem: Chap): Boolean {
            return oldItem == newItem
        }
    }

    sealed class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        class ItemBookChapView(val view: ItemBookChapViewBinding) :
            ViewHolder(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemBookChapViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder.ItemBookChapView(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder){
            is ViewHolder.ItemBookChapView ->{

            }
        }
    }
}