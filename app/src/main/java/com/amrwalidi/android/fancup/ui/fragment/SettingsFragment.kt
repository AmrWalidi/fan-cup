package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentSettingsBinding
import com.amrwalidi.android.fancup.databinding.LanguagePopupBinding
import com.amrwalidi.android.fancup.databinding.PopupMessageBinding
import com.amrwalidi.android.fancup.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModel.Factory(requireActivity().application)
        )[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.popup.observe(viewLifecycleOwner) {
            val popUpPanel = when (it) {
                1 -> languagePanel { }
                2 -> popUp(
                    getString(R.string.logout),
                    getString(R.string.Are_you_sure_you_want_to_logout),
                    getString(R.string.logout)
                ) {
                    viewModel.signOut()
                }

                3 -> popUp(
                    getString(R.string.This_account_will_be_deleted),
                    getString(R.string.All_your_account_data_will_be_deleted_permanently),
                    getString(R.string.delete)
                ) {
                    viewModel.deleteAccount()
                }

                else -> return@observe
            }

            popUpPanel.show()
        }


        return binding.root
    }

    private fun languagePanel(action: () -> Unit): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding: LanguagePopupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.language_popup,
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

        binding.confirmButton.setOnClickListener { action() }
        binding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        binding.viewModel = viewModel

        return dialog

    }


    private fun popUp(
        title: String,
        message: String,
        buttonText: String,
        action: () -> Unit
    ): Dialog {
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

        binding.messageTitle.text = title
        binding.detailedMessage.text = message
        binding.messageButton.text = buttonText
        binding.messageButton.setOnClickListener { action() }
        binding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        return dialog

    }
}