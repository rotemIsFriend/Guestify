package com.example.guestify.data.repository

import android.app.Application
import com.example.guestify.data.local_db.EventDataBase
import com.example.guestify.data.local_db.EventDao
import com.example.guestify.data.model.Event

class EventRepository(application: Application) {

    private var eventDao : EventDao?

    init {
        val db = EventDataBase.getDatabase(application.applicationContext)
        eventDao = db.eventDao()
    }

    fun getEvents() = eventDao?.getEvents()

    fun addEvent(event: Event){
        eventDao?.addEvent(event)
    }

    fun deleteEvent(id: Int){
        eventDao?.deleteEvent(id)
    }

    fun getEvent(id: Int) =  eventDao?.getEvent(id)

    fun updateEvent(event: Event) {
        eventDao?.updateEvent(event)
    }
}