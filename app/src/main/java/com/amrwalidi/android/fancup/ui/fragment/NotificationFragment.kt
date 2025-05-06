package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amrwalidi.android.fancup.R
import com.amrwalidi.android.fancup.adapters.NotificationAdapter
import com.amrwalidi.android.fancup.databinding.FragmentNotificationBinding
import com.amrwalidi.android.fancup.viewmodel.NotificationViewModel

class NotificationFragment(val type: Int) : Fragment() {

    private val viewModel: NotificationViewModel by lazy {
        ViewModelProvider(
            this,
            NotificationViewModel.Factory(type, requireActivity().application)
        )[NotificationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentNotificationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)

        val adapter = NotificationAdapter(type, viewModel)
        binding.notificationList.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

}