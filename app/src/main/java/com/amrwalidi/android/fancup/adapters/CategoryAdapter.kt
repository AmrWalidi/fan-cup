package com.amrwalidi.android.fancup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.CategoryContainerBinding
import com.amrwalidi.android.fancup.domain.Category
import androidx.recyclerview.widget.ListAdapter
import com.amrwalidi.android.fancup.viewmodel.ChallengeViewModel

class CategoryAdapter(private val viewModel: ChallengeViewModel) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent, viewModel)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CategoryViewHolder private constructor(
        private val binding: CategoryContainerBinding,
        private val viewModel: ChallengeViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Category
        ) {
            binding.viewModel = viewModel
            binding.category = item
        }

        companion object {
            fun from(parent: ViewGroup, viewModel: ChallengeViewModel): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryContainerBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding, viewModel)
            }
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}