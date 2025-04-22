package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentRegisterBinding
import com.amrwalidi.android.fancup.databinding.LoadingDialogBinding
import com.amrwalidi.android.fancup.viewmodel.RegisterViewModel


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

        val loadingDialog = loadingDialog()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        }

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

    private fun loadingDialog(): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: LoadingDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.loading_dialog,
            null,
            false
        )

        dialog.setContentView(binding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.loadingAnimation.playAnimation()

        dialog.setCancelable(false)

        return dialog
    }

}