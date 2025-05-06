package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.UserAdapter
import com.amrwalidi.android.fancup.databinding.FragmentFindFriendBinding
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import com.amrwalidi.android.fancup.viewmodel.FriendActionViewModel

class FriendRequestFragment : Fragment() {

    private val viewModel: FriendActionViewModel by lazy {
        ViewModelProvider(
            this, FriendActionViewModel.Factory(requireActivity().application)
        )[FriendActionViewModel::class.java]
    }

    private val appViewModel: AppViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFindFriendBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_friend, container, false)

        binding.viewModel = viewModel
        val adapter = UserAdapter(
            appViewModel.user.value?.id ?: "",
            getString(R.string.request),
            "${appViewModel.user.value?.username} has sent a friend request",
            1,
            viewModel
        )
        binding.userList.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.searchedUser.observe(viewLifecycleOwner) {
            viewModel.getUsers(it)
        }

        binding.searchUsername.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchTextChanged(text.toString())
        }

        viewModel.notificationMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.resetMessage()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingContainer.visibility = View.VISIBLE
            } else binding.loadingContainer.visibility = View.GONE
        }
        return binding.root
    }

}