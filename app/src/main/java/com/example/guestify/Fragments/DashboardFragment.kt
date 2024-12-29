package com.example.guestify.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guestify.EventManager
import com.example.guestify.R
import com.example.guestify.databinding.DashboardBinding
import com.example.guestify.Adapters.EventAdapter

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val events = EventManager.getEventList()

        binding.recyclerView.adapter = EventAdapter(events)


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
