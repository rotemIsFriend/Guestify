package com.example.guestify.ui.Fragments

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
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
import java.io.IOException

// Fragment that allows the user to choose a pre-made or custom invitation template.
class ChooseTemplateFragment : Fragment() {

    // ViewBinding for the fragment layout.
    private var _binding: ChooseTemplateBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel that holds and manages event data.
    private val eventsViewModel: EventsViewModel by activityViewModels()

    // Holds the custom image selected by the user (if applicable).
    lateinit var customImageBitmap: Bitmap

    // Holds the current event ID if updating an existing event.
    private var eventId: Int? = null

    // Registers a launcher to pick an image from the device’s storage.
    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val bitmapConverterResult = uriToBitmap(uri, requireContext().contentResolver)
                if (bitmapConverterResult != null) {
                    customImageBitmap = bitmapConverterResult
                    binding.customImg.isEnabled = true
                    binding.customImg.setImageBitmap(customImageBitmap)
                }
            }
        }

    // Inflates the layout for this fragment and retrieves the event ID (if any) from arguments.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChooseTemplateBinding.inflate(inflater, container, false)
        eventId = arguments?.getInt("eventId")
        return binding.root
    }

    // Sets up the fragment once the view is created: generates previews, sets up click listeners, etc.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val invitationData = getInvitationData()

        val bindingT1 = InviteTemplate1Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT2 = InviteTemplate2Binding.inflate(LayoutInflater.from(requireContext()), null, false)
        val bindingT3 = InviteTemplate3Binding.inflate(LayoutInflater.from(requireContext()), null, false)

        // Generate preview bitmaps for each available template.
        val previewBitmap = generateInvitationPreview(invitationData, bindingT1, bindingT2, bindingT3)

        // Set the preview images on the respective template buttons.
        binding.ibtnTemplate1.setImageBitmap(previewBitmap[0])
        binding.ibtnTemplate2.setImageBitmap(previewBitmap[1])
        binding.ibtnTemplate3.setImageBitmap(previewBitmap[2])

        // Handle clicks on each template button to confirm selection.
        binding.ibtnTemplate1.setOnClickListener {
            showConfirmationDialog(previewBitmap[0], invitationData)
        }

        binding.ibtnTemplate2.setOnClickListener {
            showConfirmationDialog(previewBitmap[1], invitationData)
        }

        binding.ibtnTemplate3.setOnClickListener {
            showConfirmationDialog(previewBitmap[2], invitationData)
        }

        // Allows the user to confirm their custom image selection.
        binding.customImg.setOnClickListener {
            showConfirmationDialog(customImageBitmap, invitationData)
        }

        // Initiates the process of picking a custom image from storage.
        binding.btnCustom.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }

    // Fetches and prepares data needed to populate the invitation templates,
    // either from an existing event or from arguments passed to the fragment.
    private fun getInvitationData(): Map<String, Any> {
        Log.d("ChooseTemplateFragment", "Event ID: $eventId")

        val data = if (eventId != null && eventId != 0) {
            // Retrieve existing event data if we're editing an event.
            val existingEvent = eventsViewModel.getEventByID(eventId!!)
            mapOf(
                "groomName" to (existingEvent?.groomName ?: ""),
                "brideName" to (existingEvent?.brideName ?: ""),
                "groomParents" to (existingEvent?.groomParents ?: ""),
                "brideParents" to (existingEvent?.brideParents ?: ""),
                "eventDate" to (existingEvent?.date ?: ""),
                "eventTime" to (existingEvent?.eventTime ?: ""),
                "eventLocation" to (existingEvent?.location ?: ""),
                "venueName" to (existingEvent?.venueName ?: ""),
                "invitationText" to (existingEvent?.invitationText ?: ""),
                "numOfGuests" to (existingEvent?.numOfGuests ?: 0)
            )
        } else {
            // Otherwise, collect data from fragment arguments for a new event.
            mapOf(
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
        }

        return data
    }

    // Generates preview bitmaps for three invitation templates using the provided data.
    private fun generateInvitationPreview(
        data: Map<String, Any>,
        bindingT1: InviteTemplate1Binding,
        bindingT2: InviteTemplate2Binding,
        bindingT3: InviteTemplate3Binding
    ): List<Bitmap> {

        // Inflate the views for each template.
        val invitationView1 = bindingT1.root
        val invitationView2 = bindingT2.root
        val invitationView3 = bindingT3.root

        // Extract the fields from the data map.
        val groomName = data["groomName"] as? String ?: ""
        val brideName = data["brideName"] as? String ?: ""
        val groomParents = data["groomParents"] as? String ?: ""
        val brideParents = data["brideParents"] as? String ?: ""
        val eventDate = data["eventDate"] as? String ?: ""
        val eventTime = data["eventTime"] as? String ?: ""
        val eventLocation = data["eventLocation"] as? String ?: ""
        val venueName = data["venueName"] as? String ?: ""
        val invitationText = data["invitationText"] as? String ?: ""

        // Populate Template 1 fields.
        bindingT1.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT1.tvGroomParentsNames.text = groomParents
        bindingT1.tvBrideParentsNames.text = brideParents
        bindingT1.tvDate.text = eventDate
        bindingT1.tvTimeCeremony.text = eventTime
        bindingT1.tvVenueAddress.text = eventLocation
        bindingT1.tvVenue.text = venueName
        bindingT1.tvInvitationIntro.text = invitationText

        // Populate Template 2 fields.
        bindingT2.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT2.tvGroomParentsNames.text = groomParents
        bindingT2.tvBrideParentsNames.text = brideParents
        bindingT2.tvDate.text = eventDate
        bindingT2.tvTimeCeremony.text = eventTime
        bindingT2.tvVenueAddress.text = eventLocation
        bindingT2.tvVenue.text = venueName
        bindingT2.tvInvitationIntro.text = invitationText

        // Populate Template 3 fields.
        bindingT3.tvBrideAndGroom.text = "$groomName & $brideName"
        bindingT3.tvGroomParentsNames.text = groomParents
        bindingT3.tvBrideParentsNames.text = brideParents
        bindingT3.tvDate.text = eventDate
        bindingT3.tvTimeCeremony.text = eventTime
        bindingT3.tvVenueAddress.text = eventLocation
        bindingT3.tvVenue.text = venueName
        bindingT3.tvInvitationIntro.text = invitationText

        // Convert each populated view into a Bitmap for preview.
        return listOf(
            generateBitmap(invitationView1),
            generateBitmap(invitationView2),
            generateBitmap(invitationView3)
        )
    }

    // Converts a given invitation layout into a Bitmap by measuring and drawing it on a Canvas.
    private fun generateBitmap(invitationView: ConstraintLayout): Bitmap {
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

        val bitmap = Bitmap.createBitmap(
            invitationView.measuredWidth,
            invitationView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        invitationView.draw(canvas)
        return bitmap
    }

    // Updates the chosen template for an existing or new event, then navigates to the next screen.
    private fun updateTemplateAndNavigate(template: Bitmap, invitationData: Map<String, Any>) {
        if (eventId != null && eventId != 0) {
            // Update an existing event in the ViewModel.
            val existingEvent = eventsViewModel.getEventByID(eventId!!)
            if (existingEvent != null) {
                existingEvent.imageBitmap = template
                eventsViewModel.updateEvent(existingEvent)

                val bundle = Bundle().apply {
                    putInt("eventId", eventId!!)
                }
                findNavController().navigate(
                    R.id.action_chooseTemplateFragment_to_eventDetailsFragment,
                    bundle
                )
            } else {
                // Show an error if the event is not found.
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.event_not_found))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            // Create a new event and add it to the ViewModel if no existing event ID is present.
            val groomName = invitationData["groomName"] as? String ?: ""
            val brideName = invitationData["brideName"] as? String ?: ""
            val eventName = "$groomName & $brideName"
            val groomParents = invitationData["groomParents"] as? String ?: ""
            val brideParents = invitationData["brideParents"] as? String ?: ""
            val eventDate = invitationData["eventDate"] as? String ?: ""
            val eventTime = invitationData["eventTime"] as? String ?: ""
            val eventLocation = invitationData["eventLocation"] as? String ?: ""
            val venueName = invitationData["venueName"] as? String ?: ""
            val invitationText = invitationData["invitationText"] as? String ?: ""
            val numOfGuests = (invitationData["numOfGuests"] as? Int) ?: 0

            val event = Event(
                eventName,
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

            eventsViewModel.addEvent(event) { newEventId ->
                val bundle = Bundle().apply {
                    putInt("eventId", newEventId.toInt()) // Convert Long to Int if needed.
                    putBoolean("isNewEvent", true)
                }
                findNavController().navigate(
                    R.id.action_chooseTemplateFragment_to_guestsFragment,
                    bundle
                )
            }
        }
    }

    // Displays a confirmation dialog before proceeding with the selected invitation template.
    private fun showConfirmationDialog(template: Bitmap, invitationData: Map<String, Any>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_template))
        builder.setMessage(getString(R.string.do_you_want_to_proceed_with_the_selected_template))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            updateTemplateAndNavigate(template, invitationData)
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Converts a content URI to a Bitmap, or null if an error occurs.
    private fun uriToBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Cleans up resources when the fragment's view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
    }
}
