package com.example.guestify.Fragments

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
import com.example.guestify.databinding.InvitationBinding
import com.example.guestify.viewModels.InvitationViewModel

class InvitationFragment: Fragment() {

    private var _binding : InvitationBinding ? = null
    private val binding get() = _binding!!
    private val invitationViewModel : InvitationViewModel by activityViewModels()

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
        var eventTime = ""
        var eventDate = ""


        // TODO outsource date&time dialog to its own function
        binding.btnSelectDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
                eventDate = selectedDate

                val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    eventTime = String.format("%02d:%02d", hourOfDay, minute)
                    binding.tvDateTime.text = "${eventDate} - ${eventTime}"
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
            datePickerDialog.show()

        }

        binding.btnSubmit.setOnClickListener {
            val groomName = binding.etGroomName.text.toString().trim()
            val brideName = binding.etBrideName.text.toString().trim()
            val eventLocation = binding.etEventLocation.text.toString().trim()
            val invitationText = binding.etInvitationText.text.toString().trim()

            // Validate form inputs
            val isValid = validateForm(
                groomName,
                brideName,
                eventDate,
                eventTime,
                eventLocation,
                invitationText
            )

            if (isValid) {
                // Submit data to ViewModel if valid
                invitationViewModel.submitInvitation(
                    groomName,
                    brideName,
                    eventDate,
                    eventTime,
                    eventLocation,
                    invitationText
                )
                Toast.makeText(requireContext(), "Invitation created!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please fix the errors above.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validateForm(
        groomName: String,
        brideName: String,
        eventDate: String,
        eventTime: String,
        eventLocation: String,
        invitationText: String
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

        // Event Date
        if (eventDate.isEmpty() || eventTime.isEmpty()) {
            binding.btnSelectDate.error = "Please pick date & time"
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
