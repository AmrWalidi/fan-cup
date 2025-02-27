package com.amrwalidi.android.fancup

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        val options = FirebaseOptions.Builder()
            .setApplicationId("1:581416832706:android:2750924cc7e33d381db55b")
            .setApiKey("AIzaSyAfFWMOJgpAD0RlLO7CDldS1hOYQJzJzXg")
            .setProjectId("fan-cup")
            .setStorageBucket("fan-cup.firebasestorage.app")
            .build()

        if (FirebaseApp.getApps(this).none { it.name == "admin" }) {
            FirebaseApp.initializeApp(this, options, "admin")
        }
    }
}