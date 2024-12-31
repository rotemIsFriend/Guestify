package com.example.guestify.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestify.EventManager
import com.example.guestify.R
import com.example.guestify.Adapters.EventAdapter
import com.example.guestify.databinding.DashboardBinding

class DashboardFragment : Fragment() {
    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardBinding.inflate(inflater, container, false)


        binding.newEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_invitationFragment)
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val events = EventManager.getEventList().toMutableList()
        binding.recyclerView.adapter = EventAdapter(events, object : EventAdapter.EventListener {
            override fun onEventClicked(index: Int) {
                val bundle = bundleOf("eventId" to events[index].eventId)
                findNavController().navigate(R.id.action_dashboardFragment_to_eventDetailsFragment, bundle)
            }

            override fun onEventDeleted(index: Int) {
                EventManager.remove(index)
                (binding.recyclerView.adapter as EventAdapter).removeEvent(index)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
