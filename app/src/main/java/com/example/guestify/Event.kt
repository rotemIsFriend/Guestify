package com.example.guestify



open class Event(
    val name: String,
    val date: String,
    val location: String,
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
    location: String
) : Event(name, date, location, eventId)

class BirthdayEvent(
    val birthdayPerson: String,
    val age: Int,
    eventId: Int,
    name: String,
    date: String,
    location: String
) : Event(name, date, location, eventId)

class ConferenceEvent(
    val topic: String,
    val speaker: String,
    eventId: Int,
    name: String,
    date: String,
    location: String
) : Event(name, date, location, eventId)


data class EventWedding(val groomName:String, val brideName:String, val eventDate:String, val eventTime:String, val eventLocation:String, val inviteText: String, val eventId:Int) {
    init {
        eventId
    }
}


