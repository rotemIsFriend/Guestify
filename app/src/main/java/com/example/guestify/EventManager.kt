package com.example.guestify

object EventManager {
    private val events: MutableList<Event> = mutableListOf()

    fun add(event: Event) {
        events.add(event)
    }

    fun remove(index: Int) {
        if (index in events.indices) {
            events.removeAt(index)
        }
    }

    fun removeById(id: Int) {
        val eventIndex = events.indexOfFirst { it.eventId == id }
        if (eventIndex != -1) {
            events.removeAt(eventIndex)
        }
    }

    fun getById(id: Int): Event? {
        return events.find { it.eventId == id }
    }

    fun getEventList(): MutableList<Event> {
        return events
    }
}
