package com.example.guestify.ui.Fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.guestify.R
import com.example.guestify.databinding.InvitationBinding
import com.example.guestify.ui.viewModels.EventsViewModel

class InvitationFragment: Fragment() {

    private var _binding : InvitationBinding ? = null
    private val binding get() = _binding!!

    private var eventTime = ""
    private var eventDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ScrollView {
        _binding = InvitationBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectDate.setOnClickListener {
            setDateTimePicker()
        }

        binding.btnSubmit.setOnClickListener {
            val groomName = binding.etGroomName.text.toString().trim()
            val brideName = binding.etBrideName.text.toString().trim()
            val groomParents = binding.etGroomPName.text.toString().trim()
            val brideParents = binding.etBridePName.text.toString().trim()
            val eventLocation = binding.etEventLocation.text.toString().trim()
            val venue = binding.etVenueName.text.toString().trim()
            val numOfGuests = binding.etNumOfGuests.text.toString().trim()
            val invitationText = binding.etInvitationText.text.toString().trim()

            // Validate form inputs
            val isValid = validateForm(
                groomName,
                brideName,
                groomParents,
                brideParents,
                eventDate,
                eventTime,
                eventLocation,
                venue,
                invitationText,
                numOfGuests,
            )

            if (isValid) {
                val bundle = Bundle().apply {
                    putString("groomName", groomName)
                    putString("brideName", brideName)
                    putString("groomParents", groomParents)
                    putString("brideParents", brideParents)
                    putString("eventDate", eventDate)
                    putString("eventTime", eventTime)
                    putString("eventLocation", eventLocation)
                    putString("venueName", venue)
                    putString("invitationText", invitationText)
                    putInt("numOfGuests", numOfGuests.toInt())
                }
                findNavController().navigate(R.id.action_invitationFragment_to_chooseTemplateFragment, bundle)
            } else {
                Toast.makeText(requireContext(), "Please fix the errors above.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setDateTimePicker() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val minDate = cal.timeInMillis

        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            eventDate = selectedDate

            val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                eventTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.tvDateTime.text = "${eventDate} - ${eventTime}"
                binding.btnSelectDate.error = null

            }

            TimePickerDialog(
                requireContext(),
                timePickerListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            datePickerListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.show()
    }

    private fun validateForm(
        groomName: String,
        brideName: String,
        groomParents: String,
        brideParents: String,
        eventDate: String,
        eventTime: String,
        eventLocation: String,
        venue: String,
        invitationText: String,
        numOfGuests: String,
    ): Boolean {
        var isValid = true

        // Groom Name
        if (groomName.isEmpty()) {
            binding.etGroomName.error = "Please enter Groom's name"
            isValid = false
        }

        // Bride Name
        if (brideName.isEmpty()) {
            binding.etBrideName.error = "Please enter Bride's name"
            isValid = false
        }

        // Groom Parents Name
        if (groomParents.isEmpty()) {
            binding.etGroomPName.error = "Please enter Groom's parents name"
            isValid = false
        }

        // Bride Parents Name
        if (brideParents.isEmpty()) {
            binding.etBridePName.error = "Please enter Bride's parents name"
            isValid = false
        }

        // Venue Name
        if (venue.isEmpty()) {
            binding.etVenueName.error = "Please enter Venue name"
            isValid = false
        }

        // Number of guests
        if (numOfGuests.isEmpty()) {
            binding.etNumOfGuests.error = "Please enter number of guests"
            isValid = false
        }

        // Event Date
        if (eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(requireContext(), "Please pick a date and time.", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Event Location
        if (eventLocation.isEmpty()) {
            binding.etEventLocation.error = "Please enter a location"
            isValid = false
        }

        // Invitation Text
        if (invitationText.isEmpty()) {
            binding.etInvitationText.error = "Invitation text cannot be empty"
            isValid = false
        }

        return isValid
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
