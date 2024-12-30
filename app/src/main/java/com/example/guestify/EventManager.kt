package com.example.guestify

object EventManager {
    private val events: MutableList<Event> = mutableListOf()

    fun add(event: Event){
        events.add(event)
    }

    fun remove(index: Int){
        events.removeAt(index)
    }

    fun getById(id: Int): Event{
        return events.find { it.eventId == id }!! // can't be null
    }

    fun getEventList(): MutableList<Event>{
        return events
    }

}