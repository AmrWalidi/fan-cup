package com.amrwalidi.android.fancup.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.LocaleManager
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.databinding.FragmentSettingsBinding
import com.amrwalidi.android.fancup.databinding.LanguagePopupBinding
import com.amrwalidi.android.fancup.databinding.PopupMessageBinding
import com.amrwalidi.android.fancup.viewmodel.SettingsViewModel
import androidx.core.content.edit
import com.amrwalidi.android.fancup.databinding.ChangeProfileImagePopupBinding
import com.amrwalidi.android.fancup.databinding.LoadingDialogBinding

class SettingsFragment : Fragment() {


    private lateinit var viewModel: SettingsViewModel
    private lateinit var changeProfileBinding: ChangeProfileImagePopupBinding
    private var image: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en"

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.Factory(lang, requireActivity().application)
        )[SettingsViewModel::class.java]

        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        var popUpPanel: Dialog? = null
        val loadingDialog = loadingDialog()

        viewModel.popup.observe(viewLifecycleOwner) {
            popUpPanel = when (it) {
                0 -> profilePopUp()

                1 -> languagePanel()

                2 -> popUp(
                    it,
                    getString(R.string.logout),
                    getString(R.string.Are_you_sure_you_want_to_logout),
                    getString(R.string.logout)
                ) {
                    viewModel.signOut()
                }

                3 -> popUp(
                    it,
                    getString(R.string.This_account_will_be_deleted),
                    getString(R.string.All_your_account_data_will_be_deleted_permanently),
                    getString(R.string.delete)
                ) { password ->
                    password?.let { p ->
                        viewModel.deleteAccount(p)
                    }
                }

                else -> return@observe
            }
            popUpPanel?.show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            popUpPanel?.dismiss()
        }



        return binding.root
    }

    private fun profilePopUp(): Dialog {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        changeProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.change_profile_image_popup,
            null,
            false
        )

        dialog.setContentView(changeProfileBinding.root)

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(true)

        changeProfileBinding.selectImage.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, 100)
        }

        changeProfileBinding.upload.setOnClickListener {
            if (image == null) {
                Toast.makeText(requireContext(), "Upload Image!!", Toast.LENGTH_SHORT).show()
            } else
                viewModel.uploadImage(requireContext(), image!!)

        }

        changeProfileBinding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        return dialog

    }

    private fun switchLanguage(context: Context, languageIndex: Int) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val languageCode = when (languageIndex) {
            0 -> "ar"
            1 -> "en"
            2 -> "tr"
            else -> return
        }
        prefs.edit { putString("language", languageCode) }
        activity?.let {
            LocaleManager.setLocale(it, languageCode)
            val intent = it.intent
            it.finish()
            it.startActivity(intent)
        }
    }

    private fun languagePanel(): Dialog {
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

        binding.confirmButton.setOnClickListener { switchLanguage(requireContext(), viewModel.languageList.value?.indexOf(true) ?: 0) }
        binding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        binding.viewModel = viewModel

        viewModel.languageList.observe(viewLifecycleOwner) { list ->
            val selectedLanguageIdx = list.indexOf(true)
            var index = 0
            binding.languageList.children.iterator().forEach { lang ->
                if (lang is AppCompatButton) {
                    if (index == selectedLanguageIdx) {
                        lang.setBackgroundResource(R.drawable.selected_language_background)
                        lang.backgroundTintList = null
                    } else {
                        lang.background = null
                        lang.backgroundTintList = ColorStateList.valueOf("545454".toInt())
                    }
                }
                index++
            }
        }

        return dialog

    }


    private fun popUp(
        idx: Int,
        title: String,
        message: String,
        buttonText: String,
        action: (String?) -> Unit
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
        if (idx == 3) {
            binding.passwordInput.visibility = View.VISIBLE
            binding.messageButton.setOnClickListener { action(binding.passwordInput.text.toString()) }
        } else {
            binding.messageButton.setOnClickListener { action(null) }
        }
        binding.closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        return dialog

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && data != null && data.data != null) {
            image = data.data
            changeProfileBinding.profileImage.setImageURI(image)
        }
    }
}