package com.example.guestify.data.repository

import android.app.Application
import com.example.guestify.data.local_db.EventDataBase
import com.example.guestify.data.local_db.GuestDao
import com.example.guestify.data.model.Guest

class GuestRepository(application: Application) {

    private var guestDao : GuestDao?

    init {
        val db = EventDataBase.getDatabase(application.applicationContext)
        guestDao = db.guestDao()
    }

    fun getGuests() = guestDao?.getGuests()

    fun addGuest(guest: Guest){
        guestDao?.addGuest(guest)
    }

    fun deleteGuest(guest: Guest){
        guestDao?.deleteGuest(guest)
    }

    fun getGuest(id: Int) =  guestDao?.getGuest(id)

    fun updateGuest(guest: Guest) {
        guestDao?.updateGuest(guest)
    }
}