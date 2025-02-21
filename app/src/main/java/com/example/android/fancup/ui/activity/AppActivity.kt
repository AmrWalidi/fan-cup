package com.example.android.fancup.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.fancup.ChallengesFragment
import com.example.android.fancup.HomeFragment
import com.example.android.fancup.R
import com.example.android.fancup.ui.fragment.SettingsFragment
import com.example.android.fancup.databinding.ActivityAppBinding
import com.example.android.fancup.ui.fragment.ChampionFragment
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
            val destination: Fragment = when (value) {
                2 -> ChampionFragment()
                3 -> HomeFragment()
                4 -> ChallengesFragment()
                5 -> SettingsFragment()
                else -> return@observe
            }

            supportFragmentManager.beginTransaction().apply {
                if (viewModel.transitionDirection.value == 1) {
                    setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                } else {
                    setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                }
                replace(R.id.mainFragment, destination)
                addToBackStack(null)
                commit()
            }
        }

        viewModel.toLoginScreen.observe(this) {
            if (it) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
    }

}