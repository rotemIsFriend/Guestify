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

class InvitationFragment : Fragment() {

    private var _binding: InvitationBinding? = null
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
            val isValid = validateInvitationFields(
                groomName,
                brideName,
                groomParents,
                brideParents,
                eventDate,
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
                findNavController().navigate(
                    R.id.action_invitationFragment_to_chooseTemplateFragment,
                    bundle
                )
            } else {
                Toast.makeText(requireContext(), "Please fix the errors above.", Toast.LENGTH_SHORT)
                    .show()
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

    private fun validateInvitationFields(
        groomName: String,
        brideName: String,
        groomParents: String,
        brideParents: String,
        dateTime: String,
        eventLocation: String,
        venue: String,
        invitationText: String,
        numOfGuests: String,
    ): Boolean {
        var isValid = true

        // -- Groom's Name (max length = 20) --
        if (groomName.isEmpty()) {
            binding.etGroomName.error = "Groom's Name cannot be empty"
            isValid = false
        } else if (groomName.length > 20) {
            binding.etGroomName.error = "Groom's Name must be at most 20 characters"
            isValid = false
        } else {
            binding.etGroomName.error = null // Clear any previous error
        }

        // -- Bride's Name (max length = 20) --
        if (brideName.isEmpty()) {
            binding.etBrideName.error = "Bride's Name cannot be empty"
            isValid = false
        } else if (brideName.length > 20) {
            binding.etBrideName.error = "Bride's Name must be at most 20 characters"
            isValid = false
        } else {
            binding.etBrideName.error = null
        }

        // -- Groom's Parents (max length = 50) --
        if (groomParents.isEmpty()) {
            binding.etGroomPName.error = "Groom's Parents cannot be empty"
            isValid = false
        } else if (groomParents.length > 50) {
            binding.etGroomPName.error = "Groom's Parents must be at most 50 characters"
            isValid = false
        } else {
            binding.etGroomPName.error = null
        }

        // -- Bride's Parents (max length = 50) --
        if (brideParents.isEmpty()) {
            binding.etBridePName.error = "Bride's Parents cannot be empty"
            isValid = false
        } else if (brideParents.length > 50) {
            binding.etBridePName.error = "Bride's Parents must be at most 50 characters"
            isValid = false
        } else {
            binding.etBridePName.error = null
        }

        // -- Event Location (max length = 100) --
        if (eventLocation.isEmpty()) {
            binding.etEventLocation.error = "Event Location cannot be empty"
            isValid = false
        } else if (eventLocation.length > 100) {
            binding.etEventLocation.error = "Event Location must be at most 100 characters"
            isValid = false
        } else {
            binding.etEventLocation.error = null
        }

        // -- Venue Name (max length = 100) --
        if (venue.isEmpty()) {
            binding.etVenueName.error = "Venue cannot be empty"
            isValid = false
        } else if (venue.length > 100) {
            binding.etVenueName.error = "Venue must be at most 100 characters"
            isValid = false
        } else {
            binding.etVenueName.error = null
        }

        // -- Number of Guests (max = 5 digits) --
        if (numOfGuests.isEmpty()) {
            binding.etNumOfGuests.error = "Number of guests cannot be empty"
            isValid = false
        } else {
            val guestCount = numOfGuests.toIntOrNull()
            if (guestCount == null || guestCount < 1) {
                binding.etNumOfGuests.error = "Please enter a valid number (> 0)"
                isValid = false
            } else if (guestCount > 99999) {
                binding.etNumOfGuests.error = "Please enter a valid number (< 100000)"
                isValid = false
            } else {
                binding.etNumOfGuests.error = null
            }
        }

        // -- Invitation Text (max = 250) --
        if (invitationText.length > 250) {
            binding.etInvitationText.error = "Invitation text must be at most 250 characters"
            isValid = false
        } else {
             if (invitationText.isEmpty()) {
                 binding.etInvitationText.error = "Invitation text cannot be empty"
                 isValid = false
             } else {
                 binding.etInvitationText.error = null
             }
            binding.etInvitationText.error = null
        }

        if (dateTime.isEmpty()) {
            // For a TextView, you can do:
            binding.tvDateTime.error = "Event Date & Time cannot be empty"
            isValid = false
        } else {
            // Clear any previous error if supported
            binding.tvDateTime.error = null
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
