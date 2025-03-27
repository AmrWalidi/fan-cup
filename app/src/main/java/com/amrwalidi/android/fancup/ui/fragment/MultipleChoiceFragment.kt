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
import com.amrwalidi.android.fancup.databinding.FragmentMultipleChoiceBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.MultipleChoiceViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel

private const val QUESTION = "QUESTION"

class MultipleChoiceFragment(private val questionViewModel: QuestionViewModel) : Fragment() {
    private var question: Question? = null
    private var multipleChoiceViewModel: MultipleChoiceViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(QUESTION)
        }
        multipleChoiceViewModel = ViewModelProvider(
            this,
            MultipleChoiceViewModel.Factory(requireActivity().application, question)
        )[MultipleChoiceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMultipleChoiceBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_multiple_choice, container, false)

        binding.viewModel = multipleChoiceViewModel
        binding.lifecycleOwner = this

        multipleChoiceViewModel?.message?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                if (it.equals("CORRECT ANSWER")) {
                    when (multipleChoiceViewModel!!.selectedAnswer.value) {
                        0 -> binding.answer1.setBackgroundResource(R.drawable.correct_answer_container)
                        1 -> binding.answer2.setBackgroundResource(R.drawable.correct_answer_container)
                        2 -> binding.answer3.setBackgroundResource(R.drawable.correct_answer_container)
                        3 -> binding.answer4.setBackgroundResource(R.drawable.correct_answer_container)
                    }
                } else {
                    when (multipleChoiceViewModel!!.selectedAnswer.value) {
                        0 -> binding.answer1.setBackgroundResource(R.drawable.wrong_answer_container)
                        1 -> binding.answer2.setBackgroundResource(R.drawable.wrong_answer_container)
                        2 -> binding.answer3.setBackgroundResource(R.drawable.wrong_answer_container)
                        3 -> binding.answer4.setBackgroundResource(R.drawable.wrong_answer_container)
                    }
                }
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        multipleChoiceViewModel?.wrongAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                questionViewModel.wrongAnswer()
                multipleChoiceViewModel!!.removeWrongAnswer()
            }
        }

        return binding.root
    }

}