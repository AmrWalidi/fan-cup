package com.amrwalidi.android.fancup.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.QuestionAdapter
import com.amrwalidi.android.fancup.databinding.FragmentGameLevelBinding
import com.amrwalidi.android.fancup.ui.activity.GameActivity
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import com.amrwalidi.android.fancup.viewmodel.GameLevelViewModel

class GameLevelFragment : Fragment() {

    private val viewModel: GameLevelViewModel by lazy {
        ViewModelProvider(
            this,
            GameLevelViewModel.Factory(requireActivity().application)
        )[GameLevelViewModel::class.java]
    }

    private val appViewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentGameLevelBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_level, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    appViewModel.navigate(3)
                }
            })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = QuestionAdapter(viewModel)
        binding.levelList.adapter = adapter

        viewModel.displayedQuestions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.selectedQuestion.observe(viewLifecycleOwner) {
            val intent = Intent(requireActivity(), GameActivity::class.java)
            intent.putExtra("QUESTION_ID", it)
            intent.putParcelableArrayListExtra("QUESTION_LIST", ArrayList(viewModel.questions))
            startActivity(intent)
        }

        return binding.root
    }

}