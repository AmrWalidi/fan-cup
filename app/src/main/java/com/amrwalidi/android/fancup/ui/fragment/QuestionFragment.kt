package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.EnterAnswersFragment
import com.amrwalidi.android.fancup.EnterNumberFragment
import com.amrwalidi.android.fancup.MultipleChoiceFragment
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentQuestionBinding
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel

class QuestionFragment : Fragment() {

    private var questionId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionId = arguments?.getLong("QUESTION_ID") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: QuestionViewModel by lazy {
            ViewModelProvider(
                this, QuestionViewModel.Factory(requireActivity().application, questionId)
            )[QuestionViewModel::class.java]
        }

        val binding: FragmentQuestionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.question.observe(viewLifecycleOwner) {

            val bundle = Bundle().apply {
                    putParcelable("QUESTION", viewModel.question.value)

            }

            val fragment = when (it.type) {
                1 -> EnterNumberFragment().apply { arguments = bundle }
                2 -> MultipleChoiceFragment().apply { arguments = bundle }
                3 -> EnterAnswersFragment().apply { arguments = bundle }
                else -> return@observe
            }

            childFragmentManager.beginTransaction().apply {
                replace(binding.questionContainer.id, fragment)
                    .commit()
            }
        }

        return binding.root
    }
}