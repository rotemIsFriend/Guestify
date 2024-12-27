package com.example.guestify.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.guestify.R
import com.example.guestify.databinding.DashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: DashboardBinding ? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardBinding.inflate(inflater, container, false)

        binding.newEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_invitationFragment)
        }

        binding.viewEventDetailsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_eventDetailsFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
