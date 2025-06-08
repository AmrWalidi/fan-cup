package com.amrwalidi.android.fancup.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController


class GameControllerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FrameLayout(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val game = activity?.intent?.getIntExtra("GAME_TYPE", 0)
        val opponent = activity?.intent?.getStringExtra("OPPONENT") ?: ""

        val direction = if (game == 1) {
            GameControllerFragmentDirections.actionGameControllerFragmentToSearchPlayerFragment()
        } else {
            GameControllerFragmentDirections.actionGameControllerFragmentToInvitedGameFragment(
                opponent
            )
        }

        view.post {
            findNavController().navigate(direction)
        }
    }
}
