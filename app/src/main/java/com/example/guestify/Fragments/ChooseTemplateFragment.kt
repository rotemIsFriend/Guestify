package com.example.guestify.Fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.guestify.databinding.ChooseTemplateBinding
import com.example.guestify.databinding.InviteTemplate1Binding
import com.example.guestify.databinding.InviteTemplate2Binding
import com.example.guestify.databinding.InviteTemplate3Binding
import com.example.guestify.viewModels.InvitationData
import com.example.guestify.viewModels.InvitationViewModel

class ChooseTemplateFragment : Fragment() {

    private var _binding : ChooseTemplateBinding ? = null
    private val binding get() = _binding!!

    private val invitationViewModel : InvitationViewModel by activityViewModels()


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
        val invitationData = invitationViewModel.invitationData

        if (invitationData == null) {
            Toast.makeText(requireContext(), "No invitation data yet!", Toast.LENGTH_SHORT).show()
            return
        }

        val bindingT1 = InviteTemplate1Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT2 = InviteTemplate2Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT3 = InviteTemplate3Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val previewBitmap = generateInvitationPreview(invitationData, bindingT1, bindingT2, bindingT3)

        //TODO set the image bitmap in the invitation viewModel, with a set fucntion
        binding.ibtnTemplate1.setImageBitmap(previewBitmap[0])
        binding.ibtnTemplate2.setImageBitmap(previewBitmap[1])
        binding.ibtnTemplate3.setImageBitmap(previewBitmap[2])

    }
    private fun generateInvitationPreview(data: InvitationData, bindingT1 : InviteTemplate1Binding, bindingT2 : InviteTemplate2Binding, bindingT3 : InviteTemplate3Binding): List<Bitmap> {
        // Use View Binding to inflate the layout
        val invitationView1 = bindingT1.root
        val invitationView2 = bindingT2.root
        val invitationView3 = bindingT3.root

        // Populate template 1 fields with the data
        bindingT1.tvBrideAndGroom.text = data.groomName + " & " + data.brideName
        bindingT1.tvGroomParentsNames.text = data.groomParents
        bindingT1.tvBrideParentsNames.text = data.brideParents
        bindingT1.tvDate.text = data.eventDate
        bindingT1.tvTimeCeremony.text = data.eventTime
        bindingT1.tvVenueAddress.text = data.eventLocation
        bindingT1.tvVenue.text = data.venueName
        bindingT1.tvInvitationIntro.text = data.invitationText

        // Populate template 2 fields with the data
        bindingT2.tvBrideAndGroom.text = data.groomName + " & " + data.brideName
        bindingT2.tvGroomParentsNames.text = data.groomParents
        bindingT2.tvBrideParentsNames.text = data.brideParents
        bindingT2.tvDate.text = data.eventDate
        bindingT2.tvTimeCeremony.text = data.eventTime
        bindingT2.tvVenueAddress.text = data.eventLocation
        bindingT2.tvVenue.text = data.venueName
        bindingT2.tvInvitationIntro.text = data.invitationText

        // Populate template 3 fields with the data
        bindingT3.tvBrideAndGroom.text = data.groomName + " & " + data.brideName
        bindingT3.tvGroomParentsNames.text = data.groomParents
        bindingT3.tvBrideParentsNames.text = data.brideParents
        bindingT3.tvDate.text = data.eventDate
        bindingT3.tvTimeCeremony.text = data.eventTime
        bindingT3.tvVenueAddress.text = data.eventLocation
        bindingT3.tvVenue.text = data.venueName
        bindingT3.tvInvitationIntro.text = data.invitationText

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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}