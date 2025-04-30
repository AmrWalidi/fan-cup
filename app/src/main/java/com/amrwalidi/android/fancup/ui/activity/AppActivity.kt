package com.amrwalidi.android.fancup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amrwalidi.android.fancup.LocaleManager
import com.amrwalidi.android.fancup.ui.fragment.PlayWithFriendFragment
import com.amrwalidi.android.fancup.ui.fragment.GameLevelFragment
import com.amrwalidi.android.fancup.ui.fragment.ChallengesFragment
import com.amrwalidi.android.fancup.ui.fragment.HomeFragment
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.ui.fragment.SettingsFragment
import com.amrwalidi.android.fancup.databinding.ActivityAppBinding
import com.amrwalidi.android.fancup.ui.fragment.ChampionFragment
import com.amrwalidi.android.fancup.ui.fragment.FriendRequestFragment
import com.amrwalidi.android.fancup.ui.fragment.NotificationFragment
import com.amrwalidi.android.fancup.viewmodel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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

        val binding: ActivityAppBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_app)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.listenForNotifications()

        viewModel.notificationList.observe(this) {
            if (it.isNotEmpty()) {
                if (it[0] > 0) {
                    binding.friendsNotificationNumberContainer.visibility = View.VISIBLE
                    binding.friendsNotificationNumber.text = it[0].toString()
                } else {
                    binding.friendsNotificationNumberContainer.visibility = View.GONE
                }

                if (it[1] > 0) {
                    binding.bellNotificationNumberContainer.visibility = View.VISIBLE
                    binding.bellNotificationNumber.text = it[1].toString()
                } else {
                    binding.bellNotificationNumberContainer.visibility = View.GONE
                }
            }

        }


        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finishAffinity()
                }
            }
        )

        viewModel.page.observe(this) { value ->
            val destination: Fragment = when (value) {
                2 -> {
                    binding.pageTitle.text = getString(R.string.champions)
                    ChampionFragment()
                }

                3 -> {
                    binding.pageTitle.text = getString(R.string.home)
                    HomeFragment()
                }

                4 -> {
                    binding.pageTitle.text = getString(R.string.challenges)
                    ChallengesFragment()
                }

                5 -> {
                    binding.pageTitle.text = getString(R.string.settings)
                    SettingsFragment()
                }

                6 -> GameLevelFragment()

                7 -> FriendRequestFragment()

                8 -> PlayWithFriendFragment()

                9 -> {
                    binding.pageTitle.text = getString(R.string.notifications)
                    NotificationFragment(1)
                }

                10 -> {
                    binding.pageTitle.text = getString(R.string.notifications)
                    NotificationFragment(2)
                }

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
                } else if (viewModel.transitionDirection.value == -1) {
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
                finishAffinity()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.setLocale(newBase, getSavedLanguage(newBase)))
    }

    private fun CoroutineScope.listenForNotifications() = launch {
        while (isActive) {
            viewModel.fetchNotifications()
            delay(3_000)
        }
    }

    private fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("language", "en") ?: "en"
    }
}