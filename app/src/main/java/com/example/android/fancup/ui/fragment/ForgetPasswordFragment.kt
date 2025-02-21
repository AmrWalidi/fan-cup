package com.example.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.fancup.R
import com.example.android.fancup.databinding.FragmentForgetPasswordBinding
import com.example.android.fancup.viewmodel.ForgetPasswordViewModel

class ForgetPasswordFragment : Fragment() {

    private val viewModel: ForgetPasswordViewModel by lazy {
        ViewModelProvider(
            this,
            ForgetPasswordViewModel.Factory(requireActivity().application)
        )[ForgetPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentForgetPasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.loginPage.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToLoginFragment())
        }
        return binding.root
    }
}