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
import com.amrwalidi.android.fancup.viewmodel.FriendRequestViewModel

class FriendRequestFragment : Fragment() {

    private val viewModel: FriendRequestViewModel by lazy {
        ViewModelProvider(
            this, FriendRequestViewModel.Factory(requireActivity().application)
        )[FriendRequestViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFindFriendBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_friend, container, false)

        binding.viewModel = viewModel
        val adapter = UserAdapter(getString(R.string.request), viewModel)
        binding.userList.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.searchedUser.observe(viewLifecycleOwner) {
            viewModel.getUsers(it)
        }
        return binding.root
    }

}