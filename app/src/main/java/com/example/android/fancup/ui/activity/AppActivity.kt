package com.example.android.fancup.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.fancup.R
import com.example.android.fancup.databinding.ActivityAppBinding
import com.example.android.fancup.viewmodel.AppViewModel

class AppActivity : AppCompatActivity() {

    private val viewModel: AppViewModel by lazy {
        ViewModelProvider(
            this,
            AppViewModel.Factory(application)
        )[AppViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity()
                }
            }
        )

        val binding: ActivityAppBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_app)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.page.observe(this) { value ->
            val navGraphId: Int = when (value) {

                1 -> R.navigation.home_nav
                2 -> R.navigation.challenges_nav
                3 -> R.navigation.champion_nav
                4 -> R.navigation.settings_nav
                else -> return@observe
            }


            val navController = findNavController(R.id.mainFragment)

            val navInflater = navController.navInflater
            val newGraph = navInflater.inflate(navGraphId)

            navController.graph = newGraph

        }
    }

}