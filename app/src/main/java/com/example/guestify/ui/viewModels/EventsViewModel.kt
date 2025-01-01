package com.example.guestify.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.guestify.data.model.Event
import com.example.guestify.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsViewModel(application: Application): AndroidViewModel(application) {

    private val repository = EventRepository(application)

    val events : LiveData<List<Event>>? = repository.getEvents()

    fun addEvent(event: Event, onResult: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newEventId = repository.addEvent(event)
            withContext(Dispatchers.Main) {
                onResult(newEventId)
            }
        }
    }

    fun deleteEvent(event: Event){
        repository.deleteEvent(event)
    }

    fun getEventByID(id: Int) = repository.getEvent(id)

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvent(event)
        }
    }
}