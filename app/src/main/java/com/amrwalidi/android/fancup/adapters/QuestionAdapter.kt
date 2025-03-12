package com.amrwalidi.android.fancup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.LevelContainerBinding
import com.amrwalidi.android.fancup.domain.Question

class QuestionAdapter :
    ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.from(parent)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position + 1)
    }

    class QuestionViewHolder private constructor(
        private val binding: LevelContainerBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Question,
            position: Int
        ) {
            binding.question = item
            binding.num = position.toString()
        }

        companion object {
            fun from(parent: ViewGroup): QuestionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LevelContainerBinding.inflate(layoutInflater, parent, false)
                return QuestionViewHolder(binding)
            }
        }
    }


    class ItemDiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
            return oldItem == newItem
        }

    }
}