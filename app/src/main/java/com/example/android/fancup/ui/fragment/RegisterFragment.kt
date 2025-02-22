package com.example.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.fancup.R
import com.example.android.fancup.databinding.FragmentRegisterBinding
import com.example.android.fancup.viewmodel.RegisterViewModel


class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            RegisterViewModel.Factory(requireActivity().application)
        )[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.loginPage.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        return binding.root
    }
}