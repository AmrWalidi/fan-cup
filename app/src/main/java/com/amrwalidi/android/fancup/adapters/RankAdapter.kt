package com.amrwalidi.android.fancup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.RankContainerBinding
import com.amrwalidi.android.fancup.domain.User

class RankAdapter(val context: Context) :
    ListAdapter<User, RankAdapter.RankViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        return RankViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(context, item)
    }

    class RankViewHolder private constructor(
        private val binding: RankContainerBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            item: User,
        ) {
            binding.user = item
            if (item.rank.toInt() == 1) {
                binding.rankPlacesIcon.visibility = View.VISIBLE
                binding.rankPlacesIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.first_place_cup)
                )
                binding.rankPlacesNumber.visibility = View.GONE
            } else if (item.rank.toInt() == 2) {
                binding.rankPlacesIcon.visibility = View.VISIBLE
                binding.rankPlacesIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.second_place_icon)
                )
                binding.rankPlacesNumber.visibility = View.GONE
            } else if (item.rank.toInt() == 3) {
                binding.rankPlacesIcon.visibility = View.VISIBLE
                binding.rankPlacesIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.third_place_icon)
                )
                binding.rankPlacesNumber.visibility = View.GONE
            } else {
                binding.rankPlacesIcon.visibility = View.GONE
            }
        }

        companion object {
            fun from(parent: ViewGroup): RankViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RankContainerBinding.inflate(layoutInflater, parent, false)
                return RankViewHolder(binding)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

    }
}