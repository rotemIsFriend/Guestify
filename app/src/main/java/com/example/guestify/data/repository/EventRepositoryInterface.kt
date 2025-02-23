package com.example.guestify.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import com.example.guestify.util.Resource
import com.example.guestify.data.model.Event

interface EventRepositoryInterface {

    suspend fun addEvent(event: Event): Resource<Void>

    suspend fun updateEvent(event: Event): Resource<Void>

    suspend fun deleteEvent(eventId: Int) : Resource<Void>

    suspend fun getEvents(): Resource<List<Event>>

    suspend  fun  getEvent(id: Int): Resource<Event>

    fun getFavoriteEventsLiveData(data : MutableLiveData<Resource<List<Event>>>)

    suspend fun updateFavoriteStatus(eventId: Int, isFavorite: Boolean) : Resource<Void>

    fun getEventsLiveData(data : MutableLiveData<Resource<List<Event>>>)

}