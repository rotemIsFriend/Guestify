package com.example.guestify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guests_table")
data class Guest (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "phone")
    var phone: String,
    @ColumnInfo(name = "eventId")
    val eventId: Int
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}