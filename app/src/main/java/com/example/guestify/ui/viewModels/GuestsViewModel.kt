package com.example.guestify.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guestify.data.model.Guest
import com.example.guestify.data.repository.GuestRepository

class GuestsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GuestRepository(application)

    val guests: LiveData<List<Guest>>? = repository.getGuests()

    fun addGuest(guest: Guest) {
        repository.addGuest(guest)
    }

    fun updateGuest(guest: Guest) {
        repository.updateGuest(guest)
    }

    fun removeGuest(guest: Guest) {
        repository.deleteGuest(guest)
    }

    fun getGuest(id: Int) {
        repository.getGuest(id)
    }
}