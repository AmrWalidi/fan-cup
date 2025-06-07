package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.bindingUtils.imageByteArray
import com.amrwalidi.android.fancup.databinding.FragmentSearchPlayerBinding
import com.amrwalidi.android.fancup.viewmodel.InvitedGameViewModel


class InvitedGameFragment : Fragment() {

    private val args: InvitedGameFragmentArgs by navArgs()

    private lateinit var viewModel: InvitedGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            InvitedGameViewModel.Factory(args.sender, requireActivity().application)
        )[InvitedGameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSearchPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_player, container, false)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.userUsername.text = user.username
                binding.userUsername.visibility = View.VISIBLE
                binding.userProfileImage.imageByteArray(user.profileImage)
            }
        }

        viewModel.opponent.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.searchedUsername.text = user.username
                binding.searchedUsername.visibility = View.VISIBLE
                binding.searchingText.visibility = View.GONE
                binding.searchedProfileImage.imageByteArray(user.profileImage)
            }
        }

        viewModel.isReady.observe(viewLifecycleOwner) {
            if (it) {
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(
                        SearchPlayerFragmentDirections.actionSearchPlayerFragmentToOnlineQuestionFragment(
                            viewModel.match, viewModel.question
                        )
                    )
                }, 1500)

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}
