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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
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
                if(validateFields()){
                    saveEventDetails(event)
                    toggleEditMode()
                }
            }
            else{
                toggleEditMode()
            }
        }

        binding.chooseTemplate.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("eventId", eventId)
            }
            findNavController().navigate(
                R.id.action_eventDetailsFragment_to_chooseTemplateFragment,
                bundle
            )
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
            binding.btnEditEvent.text = getString(R.string.save)
        } else {
            enableEditing(false)
            binding.btnEditEvent.text = getString(R.string.edit_event)
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
        event.name = getString(R.string.wedding3, event.groomName, event.brideName)
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

        val datePicker =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
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
            binding.eventTime.setText(
                "${
                    String.format(
                        "%02d",
                        selectedHour
                    )
                }:${String.format("%02d", selectedMinute)}"
            )
        }, hour, minute, true).show()
    }

    private fun validateFields(): Boolean {
        var isValid = true

        // 1) Bride's Name (max length = 20)
        val brideNameStr = binding.bridessName.text?.toString()?.trim().orEmpty()
        if (brideNameStr.isEmpty()) {
            binding.bridessName.error = getString(R.string.bride_s_name_cannot_be_empty)
            isValid = false
        } else if (brideNameStr.length > 20) {
            binding.bridessName.error =
                getString(R.string.bride_s_name_must_be_at_most_20_characters)
            isValid = false
        }

        // 2) Bride's Parents (max length = 50)
        val brideParentsStr = binding.bridesParents.text?.toString()?.trim().orEmpty()
        if (brideParentsStr.isEmpty()) {
            binding.bridesParents.error = getString(R.string.bride_s_parents_cannot_be_empty)
            isValid = false
        } else if (brideParentsStr.length > 50) {
            binding.bridesParents.error =
                getString(R.string.bride_s_parents_name_must_be_at_most_50_characters)
            isValid = false
        }

        // 3) Groom's Name (max length = 20)
        val groomNameStr = binding.groomsName.text?.toString()?.trim().orEmpty()
        if (groomNameStr.isEmpty()) {
            binding.groomsName.error = getString(R.string.groom_s_name_cannot_be_empty)
            isValid = false
        } else if (groomNameStr.length > 20) {
            binding.groomsName.error =
                getString(R.string.groom_s_name_must_be_at_most_20_characters)
            isValid = false
        }

        // 4) Groom's Parents (max length = 50)
        val groomParentsStr = binding.groomsParents.text?.toString()?.trim().orEmpty()
        if (groomParentsStr.isEmpty()) {
            binding.groomsParents.error = getString(R.string.groom_s_parents_cannot_be_empty)
            isValid = false
        } else if (groomParentsStr.length > 50) {
            binding.groomsParents.error =
                getString(R.string.groom_s_parents_name_must_be_at_most_50_characters)
            isValid = false
        }

        // 5) Event Hall (max length = 20)
        val eventVenueStr = binding.eventVenue.text?.toString()?.trim().orEmpty()
        if (eventVenueStr.isEmpty()) {
            binding.eventVenue.error = getString(R.string.event_hall_cannot_be_empty)
            isValid = false
        } else if (eventVenueStr.length > 20) {
            binding.eventVenue.error = getString(R.string.event_hall_must_be_at_most_20_characters)
            isValid = false
        }

        // 6) Event Location (max length = 50)
        val eventLocationStr = binding.location.text?.toString()?.trim().orEmpty()
        if (eventLocationStr.isEmpty()) {
            binding.location.error = getString(R.string.event_location_cannot_be_empty)
            isValid = false
        } else if (eventLocationStr.length > 50) {
            binding.location.error =
                getString(R.string.event_location_must_be_at_most_50_characters)
            isValid = false
        }

        // 7) Amount of Guests (max length = 5 digits)
        val amountStr = binding.amount.text?.toString()?.trim().orEmpty()
        if (amountStr.isEmpty()) {
            binding.amount.error = getString(R.string.amount_of_guests_cannot_be_empty)
            isValid = false
        } else {
            val guestCount = amountStr.toIntOrNull()
            // 7a) Basic numeric validation
            if (guestCount == null || guestCount < 1) {
                binding.amount.error = getString(R.string.please_enter_a_valid_number_0)
                isValid = false
            }
            // 7b) Check upper limit
            else if (guestCount > 99999) {
                binding.amount.error = getString(R.string.please_enter_a_valid_number_100_000)
                isValid = false
            }
        }

        return isValid
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
