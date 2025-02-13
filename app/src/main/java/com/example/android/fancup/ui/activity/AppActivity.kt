package com.example.android.fancup.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.fancup.ChallengesFragment
import com.example.android.fancup.ChampionFragment
import com.example.android.fancup.HomeFragment
import com.example.android.fancup.R
import com.example.android.fancup.SettingsFragment
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
            val fragment: Fragment = when (value) {

                1 -> HomeFragment()
                2 -> ChallengesFragment()
                3 -> ChampionFragment()
                4 -> SettingsFragment()
                else -> return@observe
            }

            val transaction = supportFragmentManager.beginTransaction()

            transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )

            transaction.replace(binding.mainFragment.id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }

}