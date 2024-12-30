package com.example.guestify.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.guestify.R
import com.example.guestify.databinding.EventDetailsBinding
import com.example.guestify.viewModels.InvitationViewModel
import java.util.Calendar

class EventDetailsFragment : Fragment() {

    private var _binding: EventDetailsBinding? = null
    private val binding get() = _binding!!
    private val invitationViewModel: InvitationViewModel by activityViewModels()
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_eventDetailsFragment_to_dashboardFragment)
            }
        })


        invitationViewModel.invitationData?.let { data ->
            binding.groomsName.setText(data.groomName)
            binding.bridessName.setText(data.brideName)
            binding.groomsParents.setText(data.groomParents)
            binding.bridesParents.setText(data.brideParents)
            binding.eventDate.setText(data.eventDate)
            binding.eventTime.setText(data.eventTime)
            binding.location.setText(data.eventLocation)
            binding.eventVenue.setText(data.venueName)
            binding.description.setText(data.invitationText)
            binding.amount.setText(data.numOfGuests)
        }


        binding.btnEditGuests.setOnClickListener {
            findNavController().navigate(R.id.action_eventDetailsFragment_to_guestsFragment)
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
                saveEventDetails()
            }
            toggleEditMode()
        }

        enableEditing(false)
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
    }

    private fun saveEventDetails() {
        val groomName = binding.groomsName.text.toString()
        val brideName = binding.bridessName.text.toString()
        val groomParents = binding.groomsParents.text.toString()
        val brideParents = binding.bridesParents.text.toString()
        val eventDate = binding.eventDate.text.toString()
        val eventTime = binding.eventTime.text.toString()
        val eventLocation = binding.location.text.toString()
        val venueName = binding.eventVenue.text.toString()
        val invitationText = binding.description.text.toString()
        val numOfGuests = binding.amount.text.toString().toInt()


        invitationViewModel.submitInvitation(
            groomName,
            brideName,
            groomParents,
            brideParents,
            eventDate,
            eventTime,
            eventLocation,
            venueName,
            invitationText,
            numOfGuests)


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
