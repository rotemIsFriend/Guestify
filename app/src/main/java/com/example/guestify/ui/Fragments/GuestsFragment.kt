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

class GuestsFragment : Fragment() {

    private var _binding: GuestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GuestsViewModel by viewModels()
    private var guestToEdit: Guest? = null
    private var eventId: Int? = null
    private var isNewEvent: Boolean? = null

    companion object {
        private const val REQUEST_CONTACTS_PERMISSION = 1
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.guestList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.guests?.observe(viewLifecycleOwner) {
            val currentEventGuests = it.filter { guest -> guest.eventId == eventId }
            binding.guestList.adapter = GuestAdapter(
                guests = currentEventGuests,
                onEditClick = { guest ->
                    editGuest(guest)
                },
                onDeleteClick = { guest ->
                    showDeleteConfirmationDialog(guest)
                }
            )
            binding.guestList.adapter
        }

        binding.addGuestButton.setOnClickListener {
            val name = binding.guestNameInput.text.toString().trim()
            val phone = binding.guestPhoneInput.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                if (guestToEdit != null) {
                    guestToEdit?.name = name
                    guestToEdit?.phone = phone
                    guestToEdit?.let {viewModel.updateGuest(it)}
                    Toast.makeText(requireContext(), "Guest updated", Toast.LENGTH_SHORT).show()
                    guestToEdit = null
                } else {
                    eventId?.let{viewModel.addGuest(Guest(name, phone, it))}
                    Toast.makeText(requireContext(), "Guest added", Toast.LENGTH_SHORT).show()
                }
                binding.guestNameInput.text.clear()
                binding.guestPhoneInput.text.clear()
            }
        }

        binding.finishBtn.setOnClickListener {
            Log.d("GuestsFragment", "isNewEvent: $isNewEvent")
            if(isNewEvent == false){
                findNavController().navigateUp()
            }
            else{
                findNavController().navigate(R.id.action_guestsFragment_to_dashboardFragment)
            }
        }

        binding.addFromContactsButton.setOnClickListener {
            checkAndRequestContactPermission()
        }
    }

    private fun editGuest(guest: Guest) {
        guestToEdit = guest
        binding.guestNameInput.setText(guest.name)
        binding.guestPhoneInput.setText(guest.phone)
    }

    private fun showDeleteConfirmationDialog(guest: Guest) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Guest")
            .setMessage("Are you sure you want to delete ${guest.name}?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.removeGuest(guest)
                Toast.makeText(requireContext(), "${guest.name} has been deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun checkAndRequestContactPermission() {
        if (requireContext().checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

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
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(name to phone)
            }
        }

        showContactsDialog(contacts)
    }

    private fun showContactsDialog(contacts: List<Pair<String, String>>) {
        val contactNames = contacts.map { "${it.first} (${it.second})" }.toTypedArray()
        val selectedItems = mutableListOf<Int>()

        AlertDialog.Builder(requireContext())
            .setTitle("Choose contacts")
            .setMultiChoiceItems(contactNames, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else {
                    selectedItems.remove(which)
                }
            }
            .setPositiveButton("Add") { _, _ ->
                selectedItems.forEach { index ->
                    val selectedContact = contacts[index]
                    eventId?.let{
                        viewModel.addGuest(Guest(selectedContact.first, selectedContact.second, it))
                    }
                }
                Toast.makeText(requireContext(), "Guests added", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
