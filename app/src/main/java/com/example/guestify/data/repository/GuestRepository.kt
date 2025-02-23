package com.example.guestify.data.repository

import com.example.guestify.data.local_db.GuestDao
import com.example.guestify.data.model.Guest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GuestRepository @Inject constructor(
    private val guestDao: GuestDao
) {

    fun getGuests() = guestDao.getGuests()

    suspend fun addGuest(guest: Guest) {
        withContext(Dispatchers.IO) {
            guestDao.addGuest(guest)
        }
    }

    suspend fun updateGuest(guest: Guest) {
        withContext(Dispatchers.IO) {
            guestDao.updateGuest(guest)
        }
    }

    suspend fun deleteGuest(guest: Guest) {
        withContext(Dispatchers.IO) {
            guestDao.deleteGuest(guest)
        }
    }


    fun getGuest(id: Int) = guestDao.getGuest(id)
}
