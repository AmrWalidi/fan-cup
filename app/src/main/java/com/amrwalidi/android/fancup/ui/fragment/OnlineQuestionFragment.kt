package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.amrwalidi.android.fancup.LocaleManager
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentQuestionBinding
import com.amrwalidi.android.fancup.databinding.GameResultPopupMessageBinding
import com.amrwalidi.android.fancup.databinding.PopupMessageBinding
import com.amrwalidi.android.fancup.ui.activity.AppActivity
import com.amrwalidi.android.fancup.viewmodel.OnlineQuestionViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class OnlineQuestionFragment : Fragment() {
    private lateinit var lang: String
    private lateinit var questionId: String
    private lateinit var matchId: String
    private lateinit var onlineQuestionViewModel: OnlineQuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: OnlineQuestionFragmentArgs by navArgs()
        questionId = args.questionId
        matchId = args.matchId

        onlineQuestionViewModel = ViewModelProvider(
            this,
            OnlineQuestionViewModel.Factory(matchId, requireActivity().application)
        )[OnlineQuestionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        lang = prefs.getString("language", "en") ?: "en"

        val questionViewModel: QuestionViewModel by lazy {
            ViewModelProvider(
                this, QuestionViewModel.Factory(questionId, lang, requireActivity().application)
            )[QuestionViewModel::class.java]
        }

        val binding: FragmentQuestionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)

        binding.viewModel = questionViewModel
        binding.lifecycleOwner = this

        lifecycleScope.listenForWinner()

        val exitGamePopUp = exitGamePopup {
            questionViewModel.exitGame()
            onlineQuestionViewModel.setWinner(
                onlineQuestionViewModel.match?.players?.firstOrNull { it.id != onlineQuestionViewModel.user?.id }?.id
                    ?: ""
            )
        }

        questionViewModel.question.observe(viewLifecycleOwner) {

            val bundle = Bundle().apply {
                putParcelable("QUESTION", questionViewModel.question.value)
            }

            val fragment = when (it.type) {
                1 -> EnterNumberFragment(questionViewModel).apply { arguments = bundle }
                2 -> MultipleChoiceFragment(questionViewModel).apply { arguments = bundle }
                3 -> EnterAnswersFragment(questionViewModel).apply { arguments = bundle }
                else -> return@observe
            }

            childFragmentManager.beginTransaction().apply {
                replace(binding.questionTypeContainer.id, fragment)
                    .commit()
            }
        }

        questionViewModel.showPopup.observe(viewLifecycleOwner) {
            if (it) {
                exitGamePopUp.show()
            }

        }

        questionViewModel.hasExitGame.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireActivity(), AppActivity::class.java)
                startActivity(intent)

            }
        }

        questionViewModel.deletedHearts.observe(viewLifecycleOwner) { deletedHearts ->
            var index = -1
            if (questionViewModel.hearts.value == 3) {
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

        questionViewModel.hearts.observe(viewLifecycleOwner) {
            if (it == 3)
                binding.hiddenHeart.visibility = View.VISIBLE
        }

        questionViewModel.completionMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                val localizedContext = LocaleManager.setLocale(requireContext(), lang)
                if (it == localizedContext.getString(R.string.congratulation)) {
                    onlineQuestionViewModel.setWinner(
                        onlineQuestionViewModel.match?.players?.firstOrNull { it.id == onlineQuestionViewModel.user?.id }?.id
                            ?: ""
                    )
                } else {
                    onlineQuestionViewModel.setWinner(
                        onlineQuestionViewModel.match?.players?.firstOrNull { it.id != onlineQuestionViewModel.user?.id }?.id
                            ?: ""
                    )
                }
                questionViewModel.calculatePoints()
            }
        }

        onlineQuestionViewModel.winner.observe(viewLifecycleOwner) {
            if (it != null) {
                questionViewModel.calculatePoints()
                val completionMessagePopup = completionMessagePopup("${it.username} has won")
                completionMessagePopup.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    onlineQuestionViewModel.updatePoints(questionViewModel.points)
                    onlineQuestionViewModel.endMatch()
                    completionMessagePopup.dismiss()
                    val intent = Intent(requireContext(), AppActivity::class.java)
                    startActivity(intent)
                }, 2500)


            }
        }

        return binding.root
    }

    private fun CoroutineScope.listenForWinner() = launch {
        while (isActive) {
            onlineQuestionViewModel.getWinner()
            delay(500)
        }
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
        binding.message.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.congratulation
            )
        )
        return dialog

    }
}