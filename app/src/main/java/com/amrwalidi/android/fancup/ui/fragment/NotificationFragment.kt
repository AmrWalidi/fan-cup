package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.NotificationAdapter
import com.amrwalidi.android.fancup.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNotificationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)

        val adapter = NotificationAdapter()
        binding.notificationList.adapter = adapter
        return binding.root
    }

}