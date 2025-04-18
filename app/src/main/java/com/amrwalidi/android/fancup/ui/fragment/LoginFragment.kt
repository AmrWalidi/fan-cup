package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentLoginBinding
import com.amrwalidi.android.fancup.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            LoginViewModel.Factory(requireActivity().application)
        )[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.registerPage.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                viewModel.resetRegisterPage()
            }
        }

        viewModel.appPage.observe(viewLifecycleOwner) {
            if (it)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAppActivity())

        }

        viewModel.forgetPasswordPage.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment())
                viewModel.resetForgetPasswordPage()
            }

        }
        return binding.root
    }

}