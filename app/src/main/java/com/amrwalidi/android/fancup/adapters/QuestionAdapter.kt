package com.amrwalidi.android.fancup.adapters

import android.annotation.SuppressLint
import com.amrwalidi.android.fancup.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.LevelContainerBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.GameLevelViewModel

class QuestionAdapter(val viewModel: GameLevelViewModel) :
    ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.from(parent, viewModel)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class QuestionViewHolder private constructor(
        private val binding: LevelContainerBinding,
        private val viewModel: GameLevelViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Question
        ) {
            binding.viewModel = viewModel
            binding.question = item

            if (!item.playable) {
                binding.gameLevelContainer.alpha = 0.5f
                binding.levelNumber.alpha = 0.5f
            } else {
                binding.gameLevelContainer.alpha = 1.0f
                binding.levelNumber.alpha = 1.0f
            }

            var index = 0
            binding.starsList.children.iterator().forEach { star ->
                val stars = item.stars
                if (star is ImageView) {
                    if (index < stars) {
                        star.setImageResource(R.drawable.filled_level_star)
                    } else
                        star.visibility = View.INVISIBLE
                }
                index++
            }
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: GameLevelViewModel): QuestionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LevelContainerBinding.inflate(layoutInflater, parent, false)
                return QuestionViewHolder(binding, viewModel)
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