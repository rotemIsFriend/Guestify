package com.example.guestify.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class GuestEntity (
    val name: String,
    val phone: String
)

class GuestsViewModel : ViewModel() {

    private val _guests = MutableLiveData<List<GuestEntity>>()
    val guests: LiveData<List<GuestEntity>> get() = _guests

    private val guestList = mutableListOf<GuestEntity>()

    fun addGuest(guest: GuestEntity) {
        guestList.add(guest)
        _guests.value = guestList
    }

    fun updateGuest(oldGuest: GuestEntity, newGuest: GuestEntity) {
        val index = guestList.indexOf(oldGuest)
        if (index != -1) {
            guestList[index] = newGuest
            _guests.value = guestList
        }
    }
}