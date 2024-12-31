package com.example.guestify.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guestify.data.model.Guest

class GuestsViewModel : ViewModel() {

    private val _guests = MutableLiveData<List<Guest>>()
    val guests: LiveData<List<Guest>> get() = _guests

    private val guestList = mutableListOf<Guest>()

    fun addGuest(guest: Guest) {
        guestList.add(guest)
        _guests.value = guestList
    }

    fun updateGuest(oldGuest: Guest, newGuest: Guest) {
        val index = guestList.indexOf(oldGuest)
        if (index != -1) {
            guestList[index] = newGuest
            _guests.value = guestList
        }
    }

    fun removeGuest(guest: Guest) {
        guestList.remove(guest)
        _guests.value = guestList
    }
}