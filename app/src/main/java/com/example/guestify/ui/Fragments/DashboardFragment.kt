package com.example.guestify.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestify.R
import com.example.guestify.ui.Adapters.EventAdapter
import com.example.guestify.databinding.DashboardBinding
import com.example.guestify.ui.viewModels.EventsViewModel

class DashboardFragment : Fragment() {

    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!
    private val eventsViewModel : EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardBinding.inflate(inflater, container, false)

        binding.newEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_invitationFragment)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        eventsViewModel.events?.observe(viewLifecycleOwner) {

            binding.recyclerView.adapter = EventAdapter(it, object : EventAdapter.EventListener {
                override fun onEventClicked(index: Int) {
                    val bundle = bundleOf("eventId" to it[index].id)
                    findNavController().navigate(R.id.action_dashboardFragment_to_eventDetailsFragment, bundle)
                }

                override fun onEventDeleted(index: Int) {
                    eventsViewModel.deleteEvent(it[index].id)
                }
            })

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
