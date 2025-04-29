package com.amrwalidi.android.fancup.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrwalidi.android.fancup.databinding.UserContainerBinding
import com.amrwalidi.android.fancup.domain.User
import com.amrwalidi.android.fancup.viewmodel.FriendActionViewModel

class UserAdapter(
    private val currentUser: String,
    private val buttonType: String,
    private val message: String,
    private val type: Int,
    private val viewModel: FriendActionViewModel
) :
    ListAdapter<User, UserAdapter.UserViewHolder>(ItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, currentUser, buttonType, type, message, viewModel)
    }

    class UserViewHolder private constructor(
        private val binding: UserContainerBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: User,
            currentUser: String,
            buttonType: String,
            type: Int,
            message: String,
            viewModel: FriendActionViewModel
        ) {
            binding.user = item
            binding.currentUser = currentUser
            binding.message = message
            binding.type = type
            binding.button.text = buttonType
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserContainerBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
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