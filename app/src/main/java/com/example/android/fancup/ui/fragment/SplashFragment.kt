package com.example.android.fancup.ui.fragment

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.fancup.R
import com.example.android.fancup.viewmodel.SplashViewModel

class SplashFragment : Fragment() {
    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(
            this,
            SplashViewModel.Factory(requireActivity().application)
        )[SplashViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            viewModel.hasUser.observe(viewLifecycleOwner) {
                if (it) findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToAppActivity()
                ) else findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToAuthenticationActivity()
                )
            }

        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}