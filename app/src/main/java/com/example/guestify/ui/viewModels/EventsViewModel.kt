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
    private val _favoriteEventsLiveData = MutableLiveData<List<Event>>()
    val favoriteEventsLiveData: LiveData<List<Event>> get() = _favoriteEventsLiveData

    private var _favoriteResourceLiveData = MutableLiveData<Resource<List<Event>>>()
    private val _resourceLiveData = MutableLiveData<Resource<List<Event>>>()




    init {
        observeFirestoreEvents()// Automatically load Firestore events
    }

    suspend fun getEventByIDFirebase(id: Int): Resource<Event> {
        return fireStoreRepo.getEvent(id) // ✅ Fetch from Firestore and return Resource<Event>
    }


    private fun observeFirestoreEvents() {
        fireStoreRepo.getEventsLiveData(_resourceLiveData)

        _resourceLiveData.observeForever { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { _eventsLiveData.postValue(it) }
                }
                is Resource.Error -> {
                    _eventsLiveData.postValue(emptyList()) // ✅ Ensure UI updates on failure
                }
                is Resource.Loading -> {} // Optional: Show loading state
            }
        }
    }


    fun addEvent(event: Event, onResult: (Long) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val newEventId = repository.addEvent(event)
            event.id= newEventId.toInt()
            fireStoreRepo.addEvent(event)
            withContext(Dispatchers.Main) {
                onResult(newEventId)
            }
        }
    }

   fun observeFavoriteEvents() {
        fireStoreRepo.getFavoriteEventsLiveData(_favoriteResourceLiveData)

        _favoriteResourceLiveData.observeForever { resource ->
            when (resource) {
                is Resource.Success -> resource.data?.let { _favoriteEventsLiveData.postValue(it) }
                is Resource.Error -> _favoriteEventsLiveData.postValue(emptyList())
                is Resource.Loading -> {}
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


    fun toggleFavorite(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            event.isFavorite = !event.isFavorite
            fireStoreRepo.updateEvent(event)
        }
    }
}

