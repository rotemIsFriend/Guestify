package com.example.guestify.ui.Fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

// Fragment that displays the dashboard with a list of events and allows navigation to create new events or view event details.
class DashboardFragment : Fragment() {

    // ViewBinding for accessing the views in the fragment's layout.
    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel to manage and observe event data.
    private val eventsViewModel: EventsViewModel by activityViewModels()

    // Inflates the fragment's layout and sets up initial UI components.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardBinding.inflate(inflater, container, false)

        // Sets up the "New Event" button to navigate to the InvitationFragment when clicked.
        binding.newEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_invitationFragment)
        }

        // Configures the RecyclerView with a linear layout manager for vertical scrolling.
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observes changes in the list of events from the ViewModel and updates the RecyclerView accordingly.
        eventsViewModel.events?.observe(viewLifecycleOwner) { eventsList ->
            binding.recyclerView.adapter = EventAdapter(eventsList, object : EventAdapter.EventListener {
                // Handles the event click to navigate to the EventDetailsFragment with the selected event ID.
                override fun onEventClicked(index: Int) {
                    val bundle = bundleOf("eventId" to eventsList[index].id)
                    findNavController().navigate(R.id.action_dashboardFragment_to_eventDetailsFragment, bundle)
                }

                // Handles the event deletion by showing a confirmation dialog.
                override fun onEventDeleted(index: Int) {
                    showConfirmationDialog(eventsList, index)
                }
            })
        }

        return binding.root
    }

    // Displays a confirmation dialog before deleting an event to prevent accidental deletions.
    private fun showConfirmationDialog(events: List<Event>, index: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.delete_event_confirmation))
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_event_this_action_cannot_be_undone))

        // If the user confirms, proceed to delete the selected event.
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            eventsViewModel.deleteEvent(events[index])
        }

        // If the user cancels, simply dismiss the dialog.
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        // Creates and shows the AlertDialog.
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Cleans up the binding when the fragment's view is destroyed to prevent memory leaks.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
