package com.example.guestify.viewModels

import androidx.lifecycle.ViewModel


data class InvitationData(
    val groomName: String,
    val brideName: String,
    val groomParents: String,
    val brideParents: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val venueName: String,
    val invitationText: String
)


class InvitationViewModel : ViewModel() {

    var invitationData : InvitationData? = null
        private set

    fun submitInvitation(groomName: String,
                         brideName: String,
                         groomParents: String,
                         brideParents: String,
                         eventDate: String,
                         eventTime: String,
                         eventLocation: String,
                         venueName: String,
                         invitationText: String) {
        this.invitationData = InvitationData(
            groomName,
            brideName,
            groomParents,
            brideParents,
            eventDate,
            eventTime,
            eventLocation,
            venueName,
            invitationText)
    }

}