package com.example.guestify.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.guestify.data.model.Event
import com.example.guestify.util.Resource
import com.example.guestify.util.SafeCall
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepositoryFirebase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // Inject FirebaseAuth to get current user
) : EventRepositoryInterface {

    // 🔥 Get current user's event collection
    private fun getUserEventRef() = firestore.collection("users")
        .document(auth.currentUser?.uid ?: throw Exception("User not logged in"))
        .collection("events")

    override suspend fun addEvent(event: Event): Resource<Void> = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val addition = userEventRef.document(event.id.toString()).set(event).await()
            Resource.Success(addition)
        }
    }

    override suspend fun updateEvent(event: Event): Resource<Void> = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val result = userEventRef.document(event.id.toString()).set(event).await()
            Resource.Success(result)
        }
    }

    override suspend fun deleteEvent(eventId: Int): Resource<Void> = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val result = userEventRef.document(eventId.toString()).delete().await()
            Resource.Success(result)
        }
    }

    override suspend fun getEvents() = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val result = userEventRef.get().await()
            val events = result.toObjects(Event::class.java)
            Resource.Success(events)
        }
    }

    override suspend fun getEvent(id: Int) : Resource<Event> = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val result = userEventRef.document(id.toString()).get().await()
            val event = result.toObject(Event::class.java)
            Resource.Success(event)
        }
    }

    override fun getFavoriteEvents(): Resource<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavoriteStatus(eventId: Int, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        SafeCall {
            val userEventRef = getUserEventRef()
            val result = userEventRef.document(eventId.toString()).update("is_favorite", isFavorite).await()
            Resource.Success(result)
        }
    }

    override fun getEventsLiveData(data: MutableLiveData<Resource<List<Event>>>) {
        data.postValue(Resource.Loading())
        getUserEventRef().orderBy("name").addSnapshotListener { snapshot, e ->
            if (e != null) {
                data.postValue(Resource.Error(e.localizedMessage))
            }
            if (snapshot != null && !snapshot.isEmpty) {
                data.postValue(Resource.Success(snapshot.toObjects(Event::class.java)))
            } else {
                data.postValue(Resource.Error("No data"))
            }
        }
    }
}
