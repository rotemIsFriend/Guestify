package com.example.guestify.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guestify.data.model.Event
import com.example.guestify.data.repository.EventRepository
import com.example.guestify.data.repository.EventRepositoryFirebase
import com.example.guestify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class EventsViewModel @Inject constructor(
    @Named("Room")
    private val repository: EventRepository,
    @Named("Firestore")
    private val fireStoreRepo : EventRepositoryFirebase

) : ViewModel() {

    private val _eventsLiveData = MutableLiveData<List<Event>>()
    val eventsLiveData: MutableLiveData<List<Event>> get() = _eventsLiveData



    init {
        observeFirestoreEvents()// Automatically load Firestore events
    }

    suspend fun getEventByIDFirebase(id: Int): Resource<Event> {
        return fireStoreRepo.getEvent(id) // ✅ Fetch from Firestore and return Resource<Event>
    }


    private fun observeFirestoreEvents() {
        val resourceLiveData = MutableLiveData<Resource<List<Event>>>()
        fireStoreRepo.getEventsLiveData(resourceLiveData) // ✅ Fetch Firestore events

        resourceLiveData.observeForever { resource -> // ✅ Observe Resource<List<Event>>
            when (resource) {
                is Resource.Success -> _eventsLiveData.postValue(resource.data!!)
                is Resource.Error -> _eventsLiveData.postValue(emptyList()) // Handle errors
                is Resource.Loading -> {} // Optional: Show loading state
            }
        }
    }

    private fun fetchFirestoreEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = fireStoreRepo.getEvents()
            if (result is Resource.Success) {
                _eventsLiveData.postValue(result.data!!)
            }
        }
    }

    fun addEvent(event: Event, onResult: (Long) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val newEventId = repository.addEvent(event)
            fireStoreRepo.addEvent(event)
            withContext(Dispatchers.Main) {
                onResult(newEventId)
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEvent(event)
            fireStoreRepo.deleteEvent(event.id)
        }
    }

    fun getEventByID(id: Int) = repository.getEvent(id)




    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEvent(event)
            fireStoreRepo.updateEvent(event)
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

