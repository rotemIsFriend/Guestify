package com.example.guestify.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.guestify.data.model.Event
import com.example.guestify.data.model.Guest

@Dao
interface GuestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGuest(guest: Guest)

    @Update
    fun updateGuest(guest: Guest)

    @Delete
    fun deleteGuest(guest: Guest)

    @Query("SELECT * FROM guests_table ORDER BY name ASC")
    fun getGuests() : LiveData<List<Guest>>

    @Query("SELECT * FROM guests_table WHERE id LIKE :id" )
    fun getGuest(id: Int): Guest
}