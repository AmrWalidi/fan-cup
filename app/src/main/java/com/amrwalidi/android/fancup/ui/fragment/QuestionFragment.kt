package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentQuestionBinding
import com.amrwalidi.android.fancup.databinding.PopupMessageBinding
import com.amrwalidi.android.fancup.ui.activity.AppActivity
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel
import androidx.core.graphics.drawable.toDrawable
import com.amrwalidi.android.fancup.LocaleManager
import com.amrwalidi.android.fancup.databinding.GameResultPopupMessageBinding
import com.amrwalidi.android.fancup.domain.Question

class QuestionFragment : Fragment() {

    private var questionId: String = ""
    private var lang: String = ""
    private lateinit var questionList: ArrayList<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionId = arguments?.getString("QUESTION_ID") ?: ""
        questionList = arguments?.getParcelableArrayList("QUESTION_LIST") ?: arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        lang = prefs.getString("language", "en") ?: "en"

        val viewModel: QuestionViewModel by lazy {
            ViewModelProvider(
                this, QuestionViewModel.Factory(questionId, lang, requireActivity().application)
            )[QuestionViewModel::class.java]
        }

        val binding: FragmentQuestionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val exitGamePopUp = exitGamePopup { viewModel.exitGame() }

        viewModel.question.observe(viewLifecycleOwner) {

            val bundle = Bundle().apply {
                putParcelable("QUESTION", viewModel.question.value)
            }

            val fragment = when (it.type) {
                1 -> EnterNumberFragment(viewModel).apply { arguments = bundle }
                2 -> MultipleChoiceFragment(viewModel).apply { arguments = bundle }
                3 -> EnterAnswersFragment(viewModel).apply { arguments = bundle }
                else -> return@observe
            }

            childFragmentManager.beginTransaction().apply {
                replace(binding.questionTypeContainer.id, fragment)
                    .commit()
            }
        }

        viewModel.showPopup.observe(viewLifecycleOwner) {
            if (it) {
                exitGamePopUp.show()
            }

        }

        viewModel.hasExitGame.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireActivity(), AppActivity::class.java)
                startActivity(intent)

            }
        }

        viewModel.deletedHearts.observe(viewLifecycleOwner) { deletedHearts ->
            var index = -1
            if (viewModel.hearts.value == 3) {
                index = 0
            }
            binding.hearts.children.iterator().forEach { child ->
                if (child is ImageView) {
                    if (index in 0..<deletedHearts) {
                        child.setImageResource(R.drawable.heart_broken)
                    } else child.setImageResource(R.drawable.heart)
                }
                index++
            }
        }

        viewModel.hearts.observe(viewLifecycleOwner) {
            if (it == 3)
                binding.hiddenHeart.visibility = View.VISIBLE
        }

        viewModel.completionMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.calculatePoints()
                val completionMessagePopup = completionMessagePopup(it)
                completionMessagePopup.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    completionMessagePopup.dismiss()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.game_container,
                            GameResultFragment(
                                viewModel,
                                questionList
                            )
                        )
                        .commit()

                }, 2500)

            }
        }

        return binding.root
    }

    private fun exitGamePopup(action: () -> Unit): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: PopupMessageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.popup_message,
            null,
            false
        )

        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)

        binding.messageTitle.text = getString(R.string.exit_game)
        binding.detailedMessage.text = getString(R.string.Are_you_sure_you_want_to_exit_the_game)
        binding.messageButton.text = getString(R.string.exit)
        binding.messageButton.setOnClickListener {
            action()
        }
        binding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        return dialog

    }

    private fun completionMessagePopup(message: String): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: GameResultPopupMessageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.game_result_popup_message,
            null,
            false
        )

        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)

        binding.message.text = message

        val localizedContext = LocaleManager.setLocale(requireContext(), lang)

        if (message == localizedContext.getString(R.string.congratulation)) {
            binding.message.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.congratulation
                )
            )
        } else {
            binding.message.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.game_over
                )
            )
        }
        return dialog

    }

}