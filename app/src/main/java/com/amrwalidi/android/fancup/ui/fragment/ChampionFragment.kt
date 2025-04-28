package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentChampionBinding
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import com.amrwalidi.android.fancup.viewmodel.ChampionViewModel

class ChampionFragment : Fragment() {

    private val viewModel: ChampionViewModel by lazy {
        ViewModelProvider(
            this, ChampionViewModel.Factory(requireActivity().application)
        )[ChampionViewModel::class.java]
    }

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentChampionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_champion, container, false)

        binding.withFriendButton.setOnClickListener {
            viewModel.navigateToPlayWithFriendPage()
        }

        viewModel.toPlayWithFriendPage.observe(viewLifecycleOwner) {
            appViewModel.navigate(8)
        }

        return binding.root
    }
}