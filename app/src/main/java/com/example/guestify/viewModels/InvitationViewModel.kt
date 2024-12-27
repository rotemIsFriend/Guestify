package com.example.guestify.viewModels

import androidx.lifecycle.ViewModel


data class InvitationData(
    val groomName: String,
    val brideName: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val invitationText: String
)


class InvitationViewModel : ViewModel() {

    var invitationData : InvitationData? = null
        private set

    fun submitInvitation(groomName: String,
                         brideName: String,
                         eventDate: String,
                         eventTime: String,
                         eventLocation: String,
                         invitationText: String) {
        this.invitationData = InvitationData(
            groomName,
            brideName,
            eventDate,
            eventTime,
            eventLocation,
            invitationText)
    }

}