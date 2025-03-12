package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.QuestionAdapter
import com.amrwalidi.android.fancup.databinding.FragmentGameLevelBinding
import com.amrwalidi.android.fancup.viewmodel.GameLevelViewModel

class GameLevelFragment : Fragment() {

    private val viewModel : GameLevelViewModel by lazy {
        ViewModelProvider(
            this,
            GameLevelViewModel.Factory(requireActivity().application)
        )[GameLevelViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentGameLevelBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_level, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = QuestionAdapter()
        binding.levelList.adapter = adapter

        viewModel.displayedQuestions.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        return binding.root
    }

}