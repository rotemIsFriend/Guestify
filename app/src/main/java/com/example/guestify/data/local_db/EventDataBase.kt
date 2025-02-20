package com.example.guestify.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.guestify.data.model.Event
import com.example.guestify.data.model.Guest

@Database(entities = arrayOf(Event::class, Guest::class), version = 1, exportSchema = false)
abstract class EventDataBase : RoomDatabase() {

    abstract fun eventDao() : EventDao
    abstract fun guestDao() : GuestDao

    companion object {

        @Volatile
        private var instance : EventDataBase? = null

        fun getDatabase(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext,EventDataBase::class.java, "events_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}

