package com.example.guestify.ui.Fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.guestify.data.model.Event
import com.example.guestify.R
import com.example.guestify.databinding.ChooseTemplateBinding
import com.example.guestify.databinding.InviteTemplate1Binding
import com.example.guestify.databinding.InviteTemplate2Binding
import com.example.guestify.databinding.InviteTemplate3Binding
import com.example.guestify.ui.viewModels.EventsViewModel

class ChooseTemplateFragment : Fragment() {

    private var _binding : ChooseTemplateBinding ? = null
    private val binding get() = _binding!!
    private val eventsViewModel : EventsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChooseTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val invitationData = mapOf(
            "groomName" to requireArguments().getString("groomName", ""),
            "brideName" to requireArguments().getString("brideName", ""),
            "groomParents" to requireArguments().getString("groomParents", ""),
            "brideParents" to requireArguments().getString("brideParents", ""),
            "eventDate" to requireArguments().getString("eventDate", ""),
            "eventTime" to requireArguments().getString("eventTime", ""),
            "eventLocation" to requireArguments().getString("eventLocation", ""),
            "venueName" to requireArguments().getString("venueName", ""),
            "invitationText" to requireArguments().getString("invitationText", ""),
            "numOfGuests" to requireArguments().getInt("numOfGuests", 0)
        )

        val bindingT1 = InviteTemplate1Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT2 = InviteTemplate2Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT3 = InviteTemplate3Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val previewBitmap = generateInvitationPreview(invitationData, bindingT1, bindingT2, bindingT3)

        binding.ibtnTemplate1.setImageBitmap(previewBitmap[0])
        binding.ibtnTemplate2.setImageBitmap(previewBitmap[1])
        binding.ibtnTemplate3.setImageBitmap(previewBitmap[2])

        binding.ibtnTemplate1.setOnClickListener {
            updateTemplateAndNavigate(previewBitmap[0], invitationData)
        }

        binding.ibtnTemplate2.setOnClickListener {
            updateTemplateAndNavigate(previewBitmap[1], invitationData)
        }

        binding.ibtnTemplate3.setOnClickListener {
            updateTemplateAndNavigate(previewBitmap[2], invitationData)
        }

    }
    private fun generateInvitationPreview(data: Map<String, Any>, bindingT1 : InviteTemplate1Binding, bindingT2 : InviteTemplate2Binding, bindingT3 : InviteTemplate3Binding): List<Bitmap> {
        // Use View Binding to inflate the layout
        val invitationView1 = bindingT1.root
        val invitationView2 = bindingT2.root
        val invitationView3 = bindingT3.root

        // cast and extract values from the map
        val groomName = data["groomName"] as? String ?: ""
        val brideName = data["brideName"] as? String ?: ""
        val groomParents = data["groomParents"] as? String ?: ""
        val brideParents = data["brideParents"] as? String ?: ""
        val eventDate = data["eventDate"] as? String ?: ""
        val eventTime = data["eventTime"] as? String ?: ""
        val eventLocation = data["eventLocation"] as? String ?: ""
        val venueName = data["venueName"] as? String ?: ""
        val invitationText = data["invitationText"] as? String ?: ""

        // Populate template 1 fields with the data
        bindingT1.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT1.tvGroomParentsNames.text = groomParents
        bindingT1.tvBrideParentsNames.text = brideParents
        bindingT1.tvDate.text = eventDate
        bindingT1.tvTimeCeremony.text = eventTime
        bindingT1.tvVenueAddress.text = eventLocation
        bindingT1.tvVenue.text = venueName
        bindingT1.tvInvitationIntro.text = invitationText

        // Populate template 2 fields with the data
        bindingT2.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT2.tvGroomParentsNames.text = groomParents
        bindingT2.tvBrideParentsNames.text = brideParents
        bindingT2.tvDate.text = eventDate
        bindingT2.tvTimeCeremony.text = eventTime
        bindingT2.tvVenueAddress.text = eventLocation
        bindingT2.tvVenue.text = venueName
        bindingT2.tvInvitationIntro.text = invitationText

        // Populate template 3 fields with the data
        bindingT3.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT3.tvGroomParentsNames.text = groomParents
        bindingT3.tvBrideParentsNames.text = brideParents
        bindingT3.tvDate.text = eventDate
        bindingT3.tvTimeCeremony.text = eventTime
        bindingT3.tvVenueAddress.text = eventLocation
        bindingT3.tvVenue.text = venueName
        bindingT3.tvInvitationIntro.text = invitationText

        return listOf(generateBitmap(invitationView1), generateBitmap(invitationView2), generateBitmap(invitationView3))

    }

    private fun generateBitmap(invitationView: ConstraintLayout): Bitmap{
        // Measure & layout the view
        invitationView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        invitationView.layout(
            0,
            0,
            invitationView.measuredWidth,
            invitationView.measuredHeight
        )

        // Draw to a Bitmap
        val bitmap = Bitmap.createBitmap(
            invitationView.measuredWidth,
            invitationView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        invitationView.draw(canvas)

        return bitmap

    }

    private fun updateTemplateAndNavigate(template: Bitmap, invitationData: Map<String, Any>){
        val groomName = invitationData["groomName"] as? String ?: ""
        val brideName = invitationData["brideName"] as? String ?: ""
        val eventName = "$groomName & $brideName Wedding"
        val groomParents = invitationData["groomParents"] as? String ?: ""
        val brideParents = invitationData["brideParents"] as? String ?: ""
        val eventDate = invitationData["eventDate"] as? String ?: ""
        val eventTime = invitationData["eventTime"] as? String ?: ""
        val eventLocation = invitationData["eventLocation"] as? String ?: ""
        val venueName = invitationData["venueName"] as? String ?: ""
        val invitationText = invitationData["invitationText"] as? String ?: ""
        val numOfGuests = (invitationData["numOfGuests"] as? Int) ?: 0

        val event = Event(eventName,
            eventDate,
            eventLocation,
            template,
            groomName,
            brideName,
            groomParents,
            brideParents,
            eventTime,
            venueName,
            invitationText,
            numOfGuests
            )

        eventsViewModel.addEvent(event)
        findNavController().navigate(R.id.action_chooseTemplateFragment_to_dashboardFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}