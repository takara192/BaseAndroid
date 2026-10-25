package com.winnix.base.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.winnix.base.databinding.ItemSampleBinding
import com.winnix.base.domain.model.SampleItem

class SampleItemAdapter : ListAdapter<SampleItem, SampleItemAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: ItemSampleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SampleItem) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<SampleItem>() {
        override fun areItemsTheSame(oldItem: SampleItem, newItem: SampleItem) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SampleItem, newItem: SampleItem) = oldItem == newItem
    }
}
