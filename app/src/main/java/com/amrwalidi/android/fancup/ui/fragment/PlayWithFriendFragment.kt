package com.amrwalidi.android.fancup.ui.fragment

import android.content.Intent
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
import com.amrwalidi.android.fancup.ui.activity.OnlineGameActivity
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import com.amrwalidi.android.fancup.viewmodel.FriendActionViewModel

class PlayWithFriendFragment : Fragment() {

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

        val adapter = UserAdapter(
            appViewModel.user.value?.id ?: "",
            getString(R.string.invite),
            "${appViewModel.user.value?.username} has sent an invitation to a game",
            2,
            viewModel
        )
        binding.userList.adapter = adapter

        viewModel.friends.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.searchedUser.observe(viewLifecycleOwner) {
            viewModel.getFriends(it)
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

        viewModel.invitation.observe(viewLifecycleOwner) { invitation ->
            val enteredMatch = invitation["enteredMatch"] as? Boolean ?: false
            val invitee = invitation["invitee"] as? String ?: ""

            if (enteredMatch) {
                val intent = Intent(requireContext(), OnlineGameActivity::class.java).apply {
                    putExtra("INVITEE", invitee)
                    putExtra("USER_TYPE", "inviter")
                    putExtra("GAME_TYPE", 2)
                }
                startActivity(intent)
            }
        }
        return binding.root
    }

}