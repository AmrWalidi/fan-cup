package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.bindingUtils.imageByteArray
import com.amrwalidi.android.fancup.databinding.FragmentSearchPlayerBinding
import com.amrwalidi.android.fancup.viewmodel.SearchPlayerViewModel

class SearchPlayerFragment : Fragment() {

    private val viewModel: SearchPlayerViewModel by lazy {
        ViewModelProvider(
            this, SearchPlayerViewModel.Factory(requireActivity().application)
        )[SearchPlayerViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSearchPlayerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_player, container, false)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.userProfileImage.imageByteArray( user.profileImage)
                viewModel.searchForPlayer()
                binding.playButton.setOnClickListener {
                    viewModel.exitLobby()
                    findNavController().navigate(
                        SearchPlayerFragmentDirections.actionSearchPlayerFragmentToSpinnerFragment(
                            user.id
                        )
                    )
                }
            }
        }

        viewModel.searchedUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.searchingText.visibility = View.GONE
                binding.searchedProfileImage.imageByteArray(user.profileImage)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.exitLobby()
    }

}