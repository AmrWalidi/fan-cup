package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentGameResultBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.GameResultViewModel


class GameResultFragment(
    private val reachedTime: Long,
    questionId: Long,
    private val questionList: ArrayList<Question>,
    points: Int
) : Fragment() {

    private val viewModel: GameResultViewModel by lazy {
        ViewModelProvider(
            this,
            GameResultViewModel.Factory(
                reachedTime,
                questionId,
                questionList,
                points,
                requireActivity().application
            )
        )[GameResultViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentGameResultBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_result, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val bundle = Bundle()
        viewModel.nextQuestion.value?.let { bundle.putLong("QUESTION_ID", it) }
        bundle.putParcelableArrayList("QUESTION_LIST", questionList)
        val questionFragment = QuestionFragment().apply { arguments = bundle }

        viewModel.toNextQuestion.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.game_container,
                        questionFragment
                    )
                    .commit()
            }
        }

        viewModel.toMenu.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().finish()
            }
        }

        return binding.root
    }
}