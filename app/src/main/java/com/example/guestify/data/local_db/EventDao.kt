package com.example.guestify.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.guestify.data.model.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM events_table ORDER BY date ASC")
    fun getEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events_table WHERE id LIKE :id")
    fun getEvent(id: Int): LiveData<Event>

    @Query("SELECT * FROM events_table WHERE is_favorite = 1 ORDER BY date ASC")
    fun getFavoriteEvents(): LiveData<List<Event>>

    @Query("UPDATE events_table SET is_favorite = :isFavorite WHERE id = :eventId")
    suspend fun updateFavoriteStatus(eventId: Int, isFavorite: Boolean)
}