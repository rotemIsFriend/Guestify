package com.example.guestify.ui.guests

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestify.R
import com.example.guestify.ui.Adapters.GuestAdapter
import com.example.guestify.data.model.Guest
import com.example.guestify.databinding.GuestsBinding
import com.example.guestify.ui.viewModels.GuestsViewModel
import dagger.hilt.android.AndroidEntryPoint

// Fragment responsible for managing and displaying the list of guests for a specific event.
@AndroidEntryPoint
class GuestsFragment : Fragment() {

    // ViewBinding for accessing UI components in the fragment's layout.
    private var _binding: GuestsBinding? = null
    private val binding get() = _binding!!

    // ViewModel for handling guest-related data and operations.
    private val viewModel: GuestsViewModel by viewModels()

    // Holds the guest currently being edited, if any.
    private var guestToEdit: Guest? = null

    // ID of the associated event.
    private var eventId: Int? = null

    // Flag indicating whether the current event is new.
    private var isNewEvent: Boolean? = null

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 1
    }

    // Inflates the fragment's layout and retrieves necessary arguments.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GuestsBinding.inflate(inflater, container, false)
        eventId = arguments?.getInt("eventId")
        isNewEvent = arguments?.getBoolean("isNewEvent")
        return binding.root
    }

    // Sets up UI components, observers, and event listeners after the view is created.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configures the RecyclerView with a linear layout manager for vertical scrolling.
        binding.guestList.layoutManager = LinearLayoutManager(requireContext())

        // Observes changes in the guests list from the ViewModel and updates the RecyclerView accordingly.
        viewModel.guests?.observe(viewLifecycleOwner) { guestsList ->
            val currentEventGuests = guestsList.filter { guest -> guest.eventId == eventId }
            binding.guestList.adapter = GuestAdapter(
                guests = currentEventGuests,
                onEditClick = { guest ->
                    editGuest(guest)
                },
                onDeleteClick = { guest ->
                    showDeleteConfirmationDialog(guest)
                }
            )
        }

        // Handles the addition of a new guest or updating an existing guest.
        binding.addGuestButton.setOnClickListener {
            val name = binding.guestNameInput.text.toString().trim()
            val phone = binding.guestPhoneInput.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_fill_in_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (guestToEdit != null) {
                    // Update the existing guest's details.
                    guestToEdit?.name = name
                    guestToEdit?.phone = phone
                    guestToEdit?.let { viewModel.updateGuest(it) }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.guest_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                    guestToEdit = null
                } else {
                    // Add a new guest to the event.
                    eventId?.let { viewModel.addGuest(Guest(name, phone, it)) }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.guest_added),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // Clear input fields after adding/updating a guest.
                binding.guestNameInput.text.clear()
                binding.guestPhoneInput.text.clear()
            }
        }

        // Handles the completion action, navigating appropriately based on whether it's a new event.
        binding.finishBtn.setOnClickListener {
            Log.d("GuestsFragment", "isNewEvent: $isNewEvent")
            if (isNewEvent == false) {
                findNavController().navigateUp()
            } else {
                findNavController().navigate(R.id.action_guestsFragment_to_dashboardFragment)
            }
        }

        // Initiates the process of adding guests from the user's contacts.
        binding.addFromContactsButton.setOnClickListener {
            checkAndRequestContactPermission()
        }
    }

    // Initiates editing mode for the selected guest by populating input fields.
    private fun editGuest(guest: Guest) {
        guestToEdit = guest
        binding.guestNameInput.setText(guest.name)
        binding.guestPhoneInput.setText(guest.phone)
    }

    // Displays a confirmation dialog before deleting a guest to prevent accidental deletions.
    private fun showDeleteConfirmationDialog(guest: Guest) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_guest))
            .setMessage(getString(R.string.are_you_sure_you_want_to_delete, guest.name))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.removeGuest(guest)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.has_been_deleted, guest.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Checks if the app has permission to read contacts and requests it if not.
    private fun checkAndRequestContactPermission() {
        if (requireContext().checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        }
    }

    // Handles the result of the permission request for reading contacts.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Loads the user's contacts and prepares them for selection.
    private fun loadContacts() {
        val contacts = mutableListOf<Pair<String, String>>()
        val resolver = requireContext().contentResolver
        val cursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(name to phone)
            }
        }

        showContactsDialog(contacts)
    }

    // Displays a dialog allowing the user to select multiple contacts to add as guests.
    private fun showContactsDialog(contacts: List<Pair<String, String>>) {
        val contactNames = contacts.map { "${it.first} (${it.second})" }.toTypedArray()
        val selectedItems = mutableListOf<Int>()

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_contacts))
            .setMultiChoiceItems(contactNames, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else {
                    selectedItems.remove(which)
                }
            }
            .setPositiveButton(getString(R.string.add)) { _, _ ->
                selectedItems.forEach { index ->
                    val selectedContact = contacts[index]
                    eventId?.let {
                        viewModel.addGuest(Guest(selectedContact.first, selectedContact.second, it))
                    }
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.guests_added),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    // Cleans up the binding when the fragment's view is destroyed to prevent memory leaks.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
