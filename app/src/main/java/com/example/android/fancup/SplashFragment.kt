package com.example.android.fancup

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToAppFragment()
            )
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}