package com.amrwalidi.android.fancup.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.RankAdapter
import com.amrwalidi.android.fancup.databinding.FragmentChampionBinding
import com.amrwalidi.android.fancup.ui.activity.OnlineGameActivity
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

        binding.lifecycleOwner = this

        val adapter = RankAdapter(requireContext())
        binding.rankList.adapter = adapter

        viewModel.users.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.withFriendButton.setOnClickListener {
            viewModel.navigateToPlayWithFriendPage()
        }

        binding.oneVsOneButton.setOnClickListener {
            val intent = Intent(requireContext(), OnlineGameActivity::class.java)
            intent.putExtra("GAME_TYPE", 1)
            startActivity(intent)
        }

        viewModel.toPlayWithFriendPage.observe(viewLifecycleOwner) {
            appViewModel.navigate(8)
        }

        return binding.root
    }
}