package com.amrwalidi.android.fancup.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.TextItemBinding

class AnswerAdapter :
    ListAdapter<String, AnswerAdapter.AnswerViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnswerViewHolder {
        return AnswerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class AnswerViewHolder private constructor(
        private val binding: TextItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: String
        ) {
            binding.answer = item
        }

        companion object {
            fun from(parent: ViewGroup): AnswerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TextItemBinding.inflate(layoutInflater, parent, false)
                return AnswerViewHolder(binding)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}