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
import com.amrwalidi.android.fancup.databinding.FragmentEnterNumberBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.EnterNumberViewModel
import com.amrwalidi.android.fancup.viewmodel.QuestionViewModel


private const val QUESTION = "QUESTION"

class EnterNumberFragment(private val questionViewModel: QuestionViewModel) : Fragment() {
    private var question: Question? = null
    private var enterNumberViewModel: EnterNumberViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(QUESTION)
        }
        enterNumberViewModel = ViewModelProvider(
            this,
            EnterNumberViewModel.Factory(requireActivity().application, question)
        )[EnterNumberViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentEnterNumberBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_enter_number, container, false)
        binding.viewModel = enterNumberViewModel
        binding.lifecycleOwner = this

        enterNumberViewModel?.message?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        enterNumberViewModel?.wrongAnswer?.observe(viewLifecycleOwner) {
            if (it) {
                questionViewModel.wrongAnswer()
                enterNumberViewModel!!.removeWrongAnswer()
            }
        }
        return binding.root
    }
}