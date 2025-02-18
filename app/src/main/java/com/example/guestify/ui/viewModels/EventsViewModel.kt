package com.example.guestify.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.guestify.data.model.Event
import com.example.guestify.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val events: LiveData<List<Event>> = repository.getEvents()

    fun addEvent(event: Event, onResult: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newEventId = repository.addEvent(event)
            withContext(Dispatchers.Main) {
                onResult(newEventId)
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(event)
        }
    }

    fun getEventByID(id: Int) = repository.getEvent(id)

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvent(event)
        }
    }

    val favoriteEvents: LiveData<List<Event>> = repository.getFavoriteEvents()

    fun toggleFavorite(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            event.isFavorite = !event.isFavorite
            repository.updateEvent(event)
        }
    }

}
