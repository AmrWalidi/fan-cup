package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.UserAdapter
import com.amrwalidi.android.fancup.databinding.FragmentFindFriendBinding
import com.amrwalidi.android.fancup.viewmodel.FriendActionViewModel

class PlayWithFriendFragment : Fragment() {

    private val viewModel: FriendActionViewModel by lazy {
        ViewModelProvider(
            this, FriendActionViewModel.Factory(requireActivity().application)
        )[FriendActionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFindFriendBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_friend, container, false)

        val adapter = UserAdapter(getString(R.string.invite), viewModel)
        binding.userList.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.searchedUser.observe(viewLifecycleOwner) {
            viewModel.getUsers(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingContainer.visibility = View.VISIBLE
            } else binding.loadingContainer.visibility = View.GONE
        }
        return binding.root
    }

}