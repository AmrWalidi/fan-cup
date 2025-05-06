package com.amrwalidi.android.fancup.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.NotifiationContainerBinding
import com.amrwalidi.android.fancup.domain.Notification
import com.amrwalidi.android.fancup.viewmodel.NotificationViewModel

class NotificationAdapter(private val type : Int, private val viewModel: NotificationViewModel) :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(type, viewModel, item)
    }

    class NotificationViewHolder private constructor(
        private val binding: NotifiationContainerBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: Int, viewModel: NotificationViewModel, item: Notification) {
            binding.notification = item
            binding.viewModel = viewModel
            if (type == 1){
                binding.acceptButton.setOnClickListener{
                    viewModel.acceptFriendRequest(item.sender?.id!!)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): NotificationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotifiationContainerBinding.inflate(layoutInflater, parent, false)
                return NotificationViewHolder(binding)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

    }
}