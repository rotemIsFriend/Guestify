package com.example.guestify

import android.graphics.Bitmap
import com.example.guestify.viewModels.InvitationData


open class Event(
    var name: String,
    var date: String,
    var location: String,
    var imageBitmap: Bitmap,
    var groomName: String = "",
    var brideName: String = "",
    var groomParents: String = "",
    var brideParents: String = "",
    var eventTime: String = "",
    var venueName: String = "",
    var invitationText: String = "",
    var numOfGuests: Int = 0,
    val eventId: Int = generateId(),
) {
    companion object {
        private var currentId: Int = 0

        fun generateId(): Int {
            currentId++
            return currentId
        }
    }

    fun toInvitationData(): InvitationData {
        return InvitationData(
            groomName, brideName, groomParents, brideParents,
            date, eventTime, location, venueName, invitationText, numOfGuests
        )
    }

    fun updateFromInvitationData(invitationData: InvitationData) {
        groomName = invitationData.groomName
        brideName = invitationData.brideName
        groomParents = invitationData.groomParents
        brideParents = invitationData.brideParents
        date = invitationData.eventDate
        eventTime = invitationData.eventTime
        location = invitationData.eventLocation
        venueName = invitationData.venueName
        invitationText = invitationData.invitationText
        numOfGuests = invitationData.numOfGuests
    }
}

