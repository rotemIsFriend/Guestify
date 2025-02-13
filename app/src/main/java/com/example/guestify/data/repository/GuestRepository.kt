package com.example.guestify.data.repository

import com.example.guestify.data.local_db.GuestDao
import com.example.guestify.data.model.Guest
import javax.inject.Inject

class GuestRepository @Inject constructor(
    private val guestDao: GuestDao
) {

    fun getGuests() = guestDao.getGuests()

    suspend fun addGuest(guest: Guest) {
        guestDao.addGuest(guest)
    }

    suspend fun updateGuest(guest: Guest) {
        guestDao.updateGuest(guest)
    }

    suspend fun deleteGuest(guest: Guest) {
        guestDao.deleteGuest(guest)
    }

    fun getGuest(id: Int) = guestDao.getGuest(id)
}
