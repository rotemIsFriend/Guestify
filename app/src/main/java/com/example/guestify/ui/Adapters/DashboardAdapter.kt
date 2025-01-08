package com.example.guestify.ui.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guestify.R
import com.example.guestify.data.model.Event

// Adapter that displays a list of events in a RecyclerView.
class EventAdapter(
    private val events: List<Event>,
    private val callback: EventListener
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // Allows communication from the Adapter to the hosting Activity/Fragment for handling clicks/deletions.
    interface EventListener {
        fun onEventClicked(index: Int)
        fun onEventDeleted(index: Int)
    }

    // Holds the views for each item in the RecyclerView and manages item click interactions.
    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            // Register a click listener for the entire item view.
            view.setOnClickListener(this)
        }

        val eventImage: ImageView = view.findViewById(R.id.eventImage)
        val eventName: TextView = view.findViewById(R.id.eventName)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val guestCount: TextView = view.findViewById(R.id.guest_count)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        // Invokes the callback method to handle click events on the item.
        override fun onClick(v: View?) {
            callback.onEventClicked(adapterPosition)
        }
    }

    // Inflates the layout for each item and creates a ViewHolder instance.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.dashboard_item, parent, false)
        return EventViewHolder(view)
    }

    // Binds data from the 'events' list to the corresponding view elements in the ViewHolder.
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name
        holder.eventDate.text = event.date
        holder.guestCount.text = event.numOfGuests.toString()

        // load image with Glide
        Glide.with(holder.itemView.context).
        load(Uri.parse(event.inviteImageUri)).
        into(holder.eventImage)

        // Triggers event deletion when the delete button is clicked.
        holder.deleteButton.setOnClickListener {
            callback.onEventDeleted(holder.adapterPosition)
        }
    }

    // Returns the total number of items to display in the RecyclerView.
    override fun getItemCount(): Int = events.size
}
