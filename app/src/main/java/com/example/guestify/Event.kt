package com.example.guestify

import android.graphics.Bitmap


open class Event(
    val name: String,
    val date: String,
    val location: String,
    val imageBitmap : Bitmap,
    val eventId: Int = generateId(),

    ) {
    companion object {
        private var currentId: Int = 0

        // Generate a new ID and increment it
        fun generateId(): Int {
            currentId++
            return currentId
        }
    }

}

class WeddingEvent(
    val brideName: String,
    val groomName: String,
    eventId: Int,
    name: String,
    date: String,
    location: String,
    image : Bitmap
) : Event(name, date, location, image, eventId )

class BirthdayEvent(
    val birthdayPerson: String,
    val age: Int,
    eventId: Int,
    name: String,
    date: String,
    location: String,
    image : Bitmap
) : Event(name, date, location, image, eventId)

class ConferenceEvent(
    val topic: String,
    val speaker: String,
    eventId: Int,
    name: String,
    date: String,
    location: String,
    image : Bitmap
) : Event(name, date, location,image ,eventId)


data class EventWedding(val groomName:String, val brideName:String, val eventDate:String, val eventTime:String, val eventLocation:String, val inviteText: String, val eventId:Int, val image: Bitmap) {
    init {
        eventId
    }
}


