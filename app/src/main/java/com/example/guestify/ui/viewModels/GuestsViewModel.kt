package com.example.guestify.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.guestify.data.model.Guest
import com.example.guestify.data.repository.GuestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuestsViewModel @Inject constructor(
    private val repository: GuestRepository
) : ViewModel() {

    val guests: LiveData<List<Guest>> = repository.getGuests()

    fun addGuest(guest: Guest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGuest(guest)
        }
    }

    fun updateGuest(guest: Guest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateGuest(guest)
        }
    }

    fun removeGuest(guest: Guest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGuest(guest)
        }
    }

    fun getGuest(id: Int) = repository.getGuest(id)
}
