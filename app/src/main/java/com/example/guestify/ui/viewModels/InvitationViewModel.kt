package com.example.guestify.ui.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
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
    val invitationText: String,
    val numOfGuests: Int,
    var template : Bitmap ? = null,
    val eventName:String = groomName + " & " + brideName + " Wedding"
)


class InvitationViewModel : ViewModel() {

    var invitationData = MutableLiveData<InvitationData>().apply {
        value = InvitationData(
            groomName = "",
            brideName = "",
            groomParents = "",
            brideParents = "",
            eventDate = "",
            eventTime = "",
            eventLocation = "",
            venueName = "",
            invitationText = "",
            numOfGuests = 0
        )
    }
        private set

    fun submitInvitation(groomName: String,
                         brideName: String,
                         groomParents: String,
                         brideParents: String,
                         eventDate: String,
                         eventTime: String,
                         eventLocation: String,
                         venueName: String,
                         invitationText: String,
                         numOfGuests: Int){
        this.invitationData.value = InvitationData(
            groomName,
            brideName,
            groomParents,
            brideParents,
            eventDate,
            eventTime,
            eventLocation,
            venueName,
            invitationText,
            numOfGuests)
    }

    fun updateTemplate(template: Bitmap) {
        invitationData.value?.template = template
    }

}