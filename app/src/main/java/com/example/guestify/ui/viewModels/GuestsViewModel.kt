package com.example.guestify.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.guestify.data.model.Guest
import com.example.guestify.data.repository.GuestRepository

// ViewModel responsible for managing and providing guest-related data to the UI.
// It interacts with the GuestRepository to perform CRUD operations on Guest objects.
class GuestsViewModel(application: Application) : AndroidViewModel(application) {

    // Instance of GuestRepository to handle data operations.
    private val repository = GuestRepository(application)

    // LiveData holding the list of all guests, observed by the UI for updates.
    val guests: LiveData<List<Guest>>? = repository.getGuests()

    /**
     * Adds a new guest to the repository.
     *
     * @param guest The Guest object to be added.
     */
    fun addGuest(guest: Guest) {
        repository.addGuest(guest)
    }

    /**
     * Updates the details of an existing guest in the repository.
     *
     * @param guest The Guest object with updated information.
     */
    fun updateGuest(guest: Guest) {
        repository.updateGuest(guest)
    }

    /**
     * Removes an existing guest from the repository.
     *
     * @param guest The Guest object to be deleted.
     */
    fun removeGuest(guest: Guest) {
        repository.deleteGuest(guest)
    }

    /**
     * Retrieves a specific guest by their ID.
     *
     * @param id The unique identifier of the guest.
     * @return The Guest object corresponding to the provided ID, or null if not found.
     */
    fun getGuest(id: Int): Guest? {
        return repository.getGuest(id)
    }
}
