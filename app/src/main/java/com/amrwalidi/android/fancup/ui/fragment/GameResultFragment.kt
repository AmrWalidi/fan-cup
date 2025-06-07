package com.amrwalidi.android.fancup.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentGameResultBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.ui.activity.AppActivity
import com.amrwalidi.android.fancup.viewmodel.GameResultViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel


class GameResultFragment(
    private val questionViewModel: QuestionViewModel,
    private val questionList: ArrayList<Question>
) : Fragment() {

    private val gameResultViewModel: GameResultViewModel by lazy {
        ViewModelProvider(
            this,
            GameResultViewModel.Factory(
                questionViewModel,
                questionList,
                requireActivity().application,
            )
        )[GameResultViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentGameResultBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_result, container, false)

        binding.viewModel = gameResultViewModel
        binding.lifecycleOwner = this

        var index = 0
        binding.starsList.children.iterator().forEach { star ->
            val stars = questionViewModel.stars
            println(stars)
            if (star is ImageView) {
                if (index < stars)
                    star.setImageResource(R.drawable.filled_star)
                else
                    star.setImageResource(R.drawable.star)
            }
            index++
        }

        index = 0
        binding.heartList.children.iterator().forEach { heart ->
            val hearts: Int = questionViewModel.hearts.value!!
            val deletedHearts: Int = questionViewModel.deletedHearts.value!!

            if (heart is ImageView) {
                if (index < (hearts - deletedHearts)) {
                    heart.setImageResource(R.drawable.heart)
                    heart.visibility = View.VISIBLE
                } else if (index < hearts) {
                    heart.setImageResource(R.drawable.empty_heart)
                }
                index++
            }
        }

        val bundle = Bundle()
        gameResultViewModel.nextQuestion.value?.let { bundle.putString("QUESTION_ID", it) }
        bundle.putParcelableArrayList("QUESTION_LIST", questionList)
        val questionFragment = QuestionFragment().apply { arguments = bundle }

        gameResultViewModel.toNextQuestion.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.game_container,
                        questionFragment
                    )
                    .commit()
            }
        }

        gameResultViewModel.toMenu.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireContext(), AppActivity::class.java)
                activity?.finish()
                startActivity(intent)
            }
        }

        return binding.root
    }
}