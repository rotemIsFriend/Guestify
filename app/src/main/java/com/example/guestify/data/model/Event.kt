package com.example.guestify.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.guestify.ui.viewModels.InvitationData

@Entity(tableName = "events_table")
data class Event(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "location")
    var location: String = "",
    @ColumnInfo(name = "image")
    var imageBitmap: Bitmap,
    @ColumnInfo(name = "groom_name")
    var groomName: String = "",
    @ColumnInfo(name = "bride_name")
    var brideName: String = "",
    @ColumnInfo(name = "groom_parents")
    var groomParents: String = "",
    @ColumnInfo(name = "bride_parents")
    var brideParents: String = "",
    @ColumnInfo(name = "event_time")
    var eventTime: String = "",
    @ColumnInfo(name = "venue_name")
    var venueName: String = "",
    @ColumnInfo(name = "invite_text")
    var invitationText: String = "",
    @ColumnInfo(name = "number_of_guests")
    var numOfGuests: Int = 0,

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}