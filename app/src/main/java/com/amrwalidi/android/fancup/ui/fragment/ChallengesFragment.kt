package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.CategoryAdapter
import com.amrwalidi.android.fancup.databinding.FragmentChallengesBinding
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import com.amrwalidi.android.fancup.viewmodel.ChallengeViewModel


class ChallengesFragment : Fragment() {

    private val challengeViewModel: ChallengeViewModel by lazy {
        ViewModelProvider(
            this,
            ChallengeViewModel.Factory(requireActivity().application)
        )[ChallengeViewModel::class.java]
    }

    private val appViewModel: AppViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentChallengesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenges, container, false)
        binding.lifecycleOwner = this

        val adapter = CategoryAdapter(challengeViewModel)
        binding.challengesList.adapter = adapter
        challengeViewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            adapter.submitList(categoryList)
        }

        challengeViewModel.selectedCategory.observe(viewLifecycleOwner) {
            appViewModel.navigate(3)
        }

        return binding.root
    }

}