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

    suspend fun addEvent(event: Event): Long{
        return eventDao!!.addEvent(event)
    }

    suspend fun deleteEvent(event: Event){
        eventDao?.deleteEvent(event)
    }

    fun getEvent(id: Int) =  eventDao?.getEvent(id)

    suspend fun updateEvent(event: Event) {
        eventDao?.updateEvent(event)
    }
}