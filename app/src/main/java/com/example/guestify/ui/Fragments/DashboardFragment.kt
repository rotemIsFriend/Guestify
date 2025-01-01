package com.example.guestify.ui.Fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestify.R
import com.example.guestify.data.model.Event
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
                    showConfirmationDialog(it, index)
                }
            })

        }
        return binding.root
    }

    private fun showConfirmationDialog(events: List<Event>, index : Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Event Confirmation")
        builder.setMessage("Are you sure you want to delete this event? This action cannot be undone.")
        builder.setPositiveButton("Yes") { dialog, which ->
            eventsViewModel.deleteEvent(events[index].id)

        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
