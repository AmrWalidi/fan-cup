package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.EnterAnswersFragment
import com.amrwalidi.android.fancup.MultipleChoiceFragment
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentQuestionBinding
import com.amrwalidi.android.fancup.databinding.PopupMessageBinding
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
        val exitGamePopUp = exitGamePopup(viewModel)

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

        viewModel.showPopup.observe(viewLifecycleOwner) {
            if (it == true && viewModel.hasExitGamePopup) {
                exitGamePopUp.show()
            } else {
                exitGamePopUp.dismiss()
            }

        }

        viewModel.hasExitGame.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().finish()
            }
        }
        return binding.root
    }

    private fun exitGamePopup(viewModel: QuestionViewModel): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: PopupMessageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.popup_message,
            null,
            false
        )

        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawableResource(R.drawable.popup_message_background)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)

        binding.messageTitle.text = getString(R.string.exit_game)
        binding.detailedMessage.text = getString(R.string.Are_you_sure_you_want_to_exit_the_game)
        binding.messageButton.text = getString(R.string.exit)

        binding.viewModel = viewModel

        return dialog

    }

}