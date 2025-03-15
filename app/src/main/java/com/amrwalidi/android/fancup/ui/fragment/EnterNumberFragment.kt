package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentEnterNumberBinding
import com.amrwalidi.android.fancup.domain.Question
import com.amrwalidi.android.fancup.viewmodel.EnterNumberViewModel


private const val QUESTION = "QUESTION"

class EnterNumberFragment : Fragment() {
    private var question: Question? = null
    private var viewModel: EnterNumberViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = it.getParcelable(QUESTION)
        }
        viewModel = ViewModelProvider(
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.questionText.text = question?.text ?: ""
        return binding.root
    }
}