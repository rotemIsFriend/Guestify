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
    fun addEvent(event: Event)

    @Update
    fun updateEvent(event: Event)

    @Query("DELETE FROM events_table WHERE id LIKE :id")
    fun deleteEvent(id : Int)

    @Query("SELECT * FROM events_table ORDER BY date ASC")
    fun getEvents() : LiveData<List<Event>>

    @Query("SELECT * FROM events_table WHERE id LIKE :id" )
    fun getEvent(id: Int): Event
}