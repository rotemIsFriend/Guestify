package com.example.guestify.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.guestify.data.model.Event
import com.example.guestify.data.repository.EventRepository

class EventsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = EventRepository(application)

    val events : LiveData<List<Event>>? = repository.getEvents()

    fun addEvent(event : Event) {
        repository.addEvent(event)
    }

    fun deleteEvent(event: Event){
        repository.deleteEvent(event)
    }

    fun getEventByID(id: Int) = repository.getEvent(id)

    fun updateEvent(event: Event){
        repository.updateEvent(event)
    }
}