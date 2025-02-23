package com.example.guestify.ui.Fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.guestify.R
import com.example.guestify.databinding.InvitationBinding
import com.example.guestify.ui.viewModels.EventsViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint

// Fragment responsible for creating and validating event invitations.
@AndroidEntryPoint
class InvitationFragment : Fragment() {

    // ViewBinding for accessing UI elements in the fragment's layout.
    private var _binding: InvitationBinding? = null
    private val binding get() = _binding!!

    // Variables to store selected event date and time.
    private var eventTime = ""
    private var eventDate = ""

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(result.data!!)
                    binding.etEventLocation.setText(place.address)

                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(requireContext(),getString(R.string.Autocomplete_error_cancel), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.Autocomplete_error_place_selection), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), getString(R.string.Autocomplete_error_Unexpected_error), Toast.LENGTH_SHORT).show()
            }
        }

    // Inflates the fragment's layout and initializes view binding.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ScrollView {
        _binding = InvitationBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Sets up UI components and event listeners after the view is created.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEventLocation.setOnClickListener {
            openPlaceAutocomplete()
        }

        // Sets up the date and time picker dialog when the select date button is clicked.
        binding.btnSelectDate.setOnClickListener {
            setDateTimePicker()
        }

        // Handles the submission of the invitation form.
        binding.btnSubmit.setOnClickListener {
            // Retrieves and trims input values from the form fields.
            val groomName = binding.etGroomName.text.toString().trim()
            val brideName = binding.etBrideName.text.toString().trim()
            val groomParents = binding.etGroomPName.text.toString().trim()
            val brideParents = binding.etBridePName.text.toString().trim()
            val eventLocation = binding.etEventLocation.text.toString().trim()
            val venue = binding.etVenueName.text.toString().trim()
            val numOfGuests = binding.etNumOfGuests.text.toString().trim()
            val invitationText = binding.etInvitationText.text.toString().trim()

            // Validates the input fields to ensure data integrity.
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
                // Prepares a bundle with the validated data to pass to the next fragment.
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
                // Navigates to the ChooseTemplateFragment with the provided data.
                findNavController().navigate(
                    R.id.action_invitationFragment_to_chooseTemplateFragment,
                    bundle
                )
            } else {
                // Shows a toast message prompting the user to fix input errors.
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fix_the_errors_above),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openPlaceAutocomplete() {
        try {
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountries(listOf("IL"))
                .build(requireContext())

            startAutocomplete.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.Autocomplete_error_launch), Toast.LENGTH_SHORT).show()
        }
    }

    // Displays DatePicker and TimePicker dialogs to allow users to select event date and time.
    private fun setDateTimePicker() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        val minDate = cal.timeInMillis

        // Listener for the DatePickerDialog to capture the selected date.
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            eventDate = selectedDate

            // Listener for the TimePickerDialog to capture the selected time.
            val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                eventTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.tvDateTime.text = "${eventDate} - ${eventTime}"
                binding.btnSelectDate.error = null
            }

            // Displays the TimePickerDialog after a date has been selected.
            TimePickerDialog(
                requireContext(),
                timePickerListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Creates and shows the DatePickerDialog with the custom theme.
        val datePickerDialog = DatePickerDialog(
            ContextThemeWrapper(requireContext(), R.style.MyDateTimePickerOverlay),
            datePickerListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.show()
    }

    // Validates the input fields to ensure all required information is correctly provided.
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
            binding.etGroomName.error = getString(R.string.groom_s_name_cannot_be_empty)
            isValid = false
        } else if (groomName.length > 20) {
            binding.etGroomName.error = getString(R.string.groom_s_name_must_be_at_most_20_characters)
            isValid = false
        } else {
            binding.etGroomName.error = null // Clear any previous error
        }

        // -- Bride's Name (max length = 20) --
        if (brideName.isEmpty()) {
            binding.etBrideName.error = getString(R.string.bride_s_name_cannot_be_empty)
            isValid = false
        } else if (brideName.length > 20) {
            binding.etBrideName.error = getString(R.string.bride_s_name_must_be_at_most_20_characters)
            isValid = false
        } else {
            binding.etBrideName.error = null
        }

        // -- Groom's Parents (max length = 50) --
        if (groomParents.isEmpty()) {
            binding.etGroomPName.error = getString(R.string.groom_s_parents_cannot_be_empty)
            isValid = false
        } else if (groomParents.length > 50) {
            binding.etGroomPName.error = getString(R.string.groom_s_parents_name_must_be_at_most_50_characters)
            isValid = false
        } else {
            binding.etGroomPName.error = null
        }

        // -- Bride's Parents (max length = 50) --
        if (brideParents.isEmpty()) {
            binding.etBridePName.error = getString(R.string.bride_s_parents_cannot_be_empty)
            isValid = false
        } else if (brideParents.length > 50) {
            binding.etBridePName.error = getString(R.string.bride_s_parents_name_must_be_at_most_50_characters)
            isValid = false
        } else {
            binding.etBridePName.error = null
        }

        // -- Event Location (max length = 100) --
        if (eventLocation.isEmpty()) {
            binding.etEventLocation.error = getString(R.string.event_location_cannot_be_empty)
            isValid = false
        } else if (eventLocation.length > 100) {
            binding.etEventLocation.error = getString(R.string.event_location_must_be_at_most_100_characters)
            isValid = false
        } else {
            binding.etEventLocation.error = null
        }

        // -- Venue Name (max length = 100) --
        if (venue.isEmpty()) {
            binding.etVenueName.error = getString(R.string.venue_cannot_be_empty)
            isValid = false
        } else if (venue.length > 100) {
            binding.etVenueName.error = getString(R.string.venue_must_be_at_most_100_characters)
            isValid = false
        } else {
            binding.etVenueName.error = null
        }

        // -- Number of Guests (max = 5 digits) --
        if (numOfGuests.isEmpty()) {
            binding.etNumOfGuests.error = getString(R.string.number_of_guests_cannot_be_empty)
            isValid = false
        } else {
            val guestCount = numOfGuests.toIntOrNull()
            if (guestCount == null || guestCount < 1) {
                binding.etNumOfGuests.error = getString(R.string.please_enter_a_valid_number_0)
                isValid = false
            } else if (guestCount > 99999) {
                binding.etNumOfGuests.error = getString(R.string.please_enter_a_valid_number_100000)
                isValid = false
            } else {
                binding.etNumOfGuests.error = null
            }
        }

        // -- Invitation Text (max = 250) --
        if (invitationText.length > 250) {
            binding.etInvitationText.error = getString(R.string.invitation_text_must_be_at_most_250_characters)
            isValid = false
        } else {
            if (invitationText.isEmpty()) {
                binding.etInvitationText.error = getString(R.string.invitation_text_cannot_be_empty)
                isValid = false
            } else {
                binding.etInvitationText.error = null
            }
            binding.etInvitationText.error = null
        }

        // -- Event Date and Time --
        if (dateTime.isEmpty()) {
            // For a TextView, set an error message.
            binding.tvDateTime.error = getString(R.string.event_date_time_cannot_be_empty)
            isValid = false
        } else {
            // Clear any previous error if supported.
            binding.tvDateTime.error = null
        }
        return isValid
    }

    // Cleans up the binding when the fragment's view is destroyed to prevent memory leaks.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}