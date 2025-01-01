package com.example.guestify.ui.Fragments
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.guestify.R
import com.example.guestify.data.model.Event
import com.example.guestify.databinding.EventDetailsBinding
import com.example.guestify.ui.viewModels.EventsViewModel
import com.example.guestify.ui.viewModels.InvitationData
import com.example.guestify.ui.viewModels.InvitationViewModel
import java.util.Calendar

class EventDetailsFragment : Fragment() {

    private var _binding: EventDetailsBinding? = null
    private val binding get() = _binding!!
    private val invitationViewModel: InvitationViewModel by viewModels()
    private val eventsViewModel: EventsViewModel by activityViewModels()
    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventId = arguments?.getInt("eventId") ?: -1
        if (eventId == -1) {
            findNavController().navigateUp()
            return
        }
        val event = eventsViewModel.getEventByID(eventId)
        if (event == null) {
            findNavController().navigateUp()
            return
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_eventDetailsFragment_to_dashboardFragment)
            }
        })

        eventsViewModel.events?.observe(viewLifecycleOwner) {
            populateFields(event)
        }

        binding.btnEditGuests.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("eventId", eventId)
                putBoolean("isNewEvent", false)
            }
            findNavController().navigate(R.id.action_eventDetailsFragment_to_guestsFragment, bundle)
        }

        binding.eventDate.setOnClickListener {
            if (isEditing) {
                showDatePicker()
            }
        }

        binding.eventTime.setOnClickListener {
            if (isEditing) {
                showTimePicker()
            }
        }

        binding.btnEditEvent.setOnClickListener {
            if (isEditing) {
                saveEventDetails(event)
            }
            toggleEditMode()
        }

        binding.chooseTemplate.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("eventId", eventId)
            }
            findNavController().navigate(R.id.action_eventDetailsFragment_to_chooseTemplateFragment, bundle)
        }

        enableEditing(false)
    }

    private fun populateFields(data: Event) {
        binding.eventName.setText(data.name)
        binding.groomsName.setText(data.groomName)
        binding.bridessName.setText(data.brideName)
        binding.groomsParents.setText(data.groomParents)
        binding.bridesParents.setText(data.brideParents)
        binding.eventDate.setText(data.date)
        binding.eventTime.setText(data.eventTime)
        binding.location.setText(data.location)
        binding.eventVenue.setText(data.venueName)
        binding.description.setText(data.invitationText)
        binding.amount.setText(data.numOfGuests.toString())
        binding.chooseTemplate.setImageBitmap(data.imageBitmap)
    }

    private fun toggleEditMode() {
        isEditing = !isEditing

        if (isEditing) {
            enableEditing(true)
            binding.btnEditEvent.text = "Save"
        } else {
            enableEditing(false)
            binding.btnEditEvent.text = "Edit Event"
        }
    }

    private fun enableEditing(enabled: Boolean) {
        binding.groomsName.isEnabled = enabled
        binding.bridessName.isEnabled = enabled
        binding.groomsParents.isEnabled = enabled
        binding.bridesParents.isEnabled = enabled
        binding.eventDate.isEnabled = enabled
        binding.eventTime.isEnabled = enabled
        binding.location.isEnabled = enabled
        binding.eventVenue.isEnabled = enabled
        binding.description.isEnabled = enabled
        binding.amount.isEnabled = enabled
    }

    private fun saveEventDetails(event: Event) {
        event.groomName = binding.groomsName.text.toString()
        event.brideName = binding.bridessName.text.toString()
        event.name = "${event.groomName} & ${event.brideName} Wedding"
        event.groomParents = binding.groomsParents.text.toString()
        event.brideParents = binding.bridesParents.text.toString()
        event.date = binding.eventDate.text.toString()
        event.eventTime = binding.eventTime.text.toString()
        event.location = binding.location.text.toString()
        event.venueName = binding.eventVenue.text.toString()
        event.invitationText = binding.description.text.toString()
        event.numOfGuests = binding.amount.text.toString().toInt()
        eventsViewModel.updateEvent(event)

        findNavController().navigate(R.id.action_eventDetailsFragment_to_dashboardFragment)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            binding.eventDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day)

        datePicker.datePicker.minDate = calendar.timeInMillis
        datePicker.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            binding.eventTime.setText("${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinute)}")
        }, hour, minute, true).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
