package com.example.guestify.data.repository

import com.example.guestify.data.local_db.EventDao
import com.example.guestify.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {
    fun getEvents() = eventDao.getEvents()

    suspend fun addEvent(event: Event): Long {
        return withContext(Dispatchers.IO) {
            eventDao.addEvent(event)
        }
    }

    suspend fun deleteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            eventDao.deleteEvent(event)
        }
    }

    fun getEvent(id: Int) = eventDao.getEvent(id)

    suspend fun updateEvent(event: Event) {
        withContext(Dispatchers.IO) {
            eventDao.updateEvent(event)
        }
    }
}
