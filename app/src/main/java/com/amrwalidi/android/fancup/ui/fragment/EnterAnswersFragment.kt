package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.AnswerAdapter
import com.amrwalidi.android.fancup.databinding.FragmentEnterAnswersBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.EnterAnswersViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel

private const val QUESTION = "QUESTION"

class EnterAnswersFragment(private val questionViewModel: QuestionViewModel) : Fragment() {
    private var question: Question? = null
    private var enterAnswersViewModel: EnterAnswersViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(QUESTION)
        }
        enterAnswersViewModel = ViewModelProvider(
            this,
            EnterAnswersViewModel.Factory(requireActivity().application, question)
        )[EnterAnswersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentEnterAnswersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_enter_answers, container, false)

        binding.viewModel = enterAnswersViewModel
        binding.lifecycleOwner = this

        val adapter = AnswerAdapter()
        binding.answerList.adapter = adapter

        enterAnswersViewModel?.answerList?.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }

        enterAnswersViewModel?.correctAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                questionViewModel.successfulCompletion()
            }
        }

        enterAnswersViewModel?.message?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        enterAnswersViewModel?.wrongAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                questionViewModel.wrongAnswer()
                enterAnswersViewModel!!.removeWrongAnswer()
            }
        }

        return binding.root
    }
}