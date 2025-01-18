package com.example.guestify.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.guestify.data.model.Event
import com.example.guestify.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel responsible for managing and providing event-related data to the UI.
// It interacts with the EventRepository to perform CRUD operations on Event objects.
class EventsViewModel(application: Application) : AndroidViewModel(application) {

    // Instance of EventRepository to handle data operations.
    private val repository = EventRepository(application)

    // LiveData holding the list of all events, observed by the UI for updates.
    val events: LiveData<List<Event>>? = repository.getEvents()

    /**
     * Adds a new event to the repository.
     *
     * @param event The Event object to be added.
     * @param onResult Callback function that receives the newly created event's ID.
     */
    fun addEvent(event: Event, onResult: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Inserts the event into the repository and retrieves its generated ID.
            val newEventId = repository.addEvent(event)
            withContext(Dispatchers.Main) {
                // Returns the new event ID to the caller on the main thread.
                onResult(newEventId)
            }
        }
    }

    /**
     * Deletes an existing event from the repository.
     *
     * @param event The Event object to be deleted.
     */
    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(event)
        }
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param id The unique identifier of the event.
     * @return The Event object corresponding to the provided ID.
     */
    fun getEventByID(id: Int) = repository.getEvent(id)

    /**
     * Updates the details of an existing event in the repository.
     *
     * @param event The Event object with updated information.
     */
    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvent(event)
        }
    }
}
