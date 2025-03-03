package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.CategoryAdapter
import com.amrwalidi.android.fancup.databinding.FragmentChallengesBinding
import com.amrwalidi.android.fancup.viewmodel.ChallengeViewModel


class ChallengesFragment : Fragment() {

    private val viewModel: ChallengeViewModel by lazy {
        ViewModelProvider(
            this,
            ChallengeViewModel.Factory(requireActivity().application)
        )[ChallengeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentChallengesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenges, container, false)
        binding.lifecycleOwner = this

        val adapter = CategoryAdapter(viewModel)
        binding.challengesList.adapter = adapter
        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            adapter.submitList(categoryList)
        }

        return binding.root
    }

}