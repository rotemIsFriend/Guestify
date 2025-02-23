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
import dagger.hilt.android.AndroidEntryPoint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


// Fragment that displays the dashboard with a list of events and allows navigation to create new events or view event details.
@AndroidEntryPoint
class DashboardFragment : Fragment() {

    // ViewBinding for accessing the views in the fragment's layout.
    private var _binding: DashboardBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel to manage and observe event data.
    private val eventsViewModel: EventsViewModel by activityViewModels()

    // SharedPreferences to store dark mode state
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var eventAdapter: EventAdapter

    // Inflates the fragment's layout and sets up initial UI components.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DashboardBinding.inflate(inflater, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("settings", 0)

        // Check if dark mode was previously enabled
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        // Apply the correct theme and button icon
        updateTheme(isDarkMode)

        // Dark mode toggle button click listener
        binding.themeToggleButton.setOnClickListener {
            val newMode = !sharedPreferences.getBoolean("dark_mode", false) // Toggle current mode
            val editor = sharedPreferences.edit()
            editor.putBoolean("dark_mode", newMode)
            editor.apply()

            // Apply theme and refresh activity
            updateTheme(newMode)
            requireActivity().recreate() // Refresh the activity to apply changes
        }

        setupRecyclerView()
        observeEvents()


        // Sets up the "New Event" button to navigate to the InvitationFragment when clicked.
        binding.newEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_invitationFragment)
        }

        binding.favEventBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_favoritesFragment)
        }


        // Configures the RecyclerView with a linear layout manager for vertical scrolling.
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observes changes in the list of events from the ViewModel and updates the RecyclerView accordingly.

        return binding.root
    }


    //Updates the app's theme and button icon based on the dark mode state.
    private fun updateTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.themeToggleButton.setImageResource(R.drawable.icons8_sun__1_) // Sun icon
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.themeToggleButton.setImageResource(R.drawable.noun_moon_7177219) // Moon icon
        }
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

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(emptyList(), eventsViewModel, object : EventAdapter.EventListener {
            override fun onEventClicked(index: Int) {
                val event = eventAdapter.getEventAt(index)
                val bundle = bundleOf("eventId" to event.id)
                findNavController().navigate(R.id.action_dashboardFragment_to_eventDetailsFragment, bundle)
            }

            override fun onEventDeleted(index: Int) {
                val event = eventAdapter.getEventAt(index) // ✅ Get event from adapter
                showConfirmationDialog(listOf(event), index) // ✅ Pass the correct event list
            }
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }



    private fun observeEvents() {
        eventsViewModel.eventsLiveData.observe(viewLifecycleOwner) { eventsList ->
            Log.d("FirestoreDebug", "Received ${eventsList.size} events from Firestore")
            eventAdapter.updateEvents(eventsList) // ✅ Update adapter dynamically
        }
    }





            // Cleans up the binding when the fragment's view is destroyed to prevent memory leaks.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}