package com.example.guestify.ui.Adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.guestify.data.model.Event
import com.example.guestify.R

class EventAdapter(
    private val events: List<Event>,
    private val callback: EventListener
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    interface EventListener {
        fun onEventClicked(index: Int)
        fun onEventDeleted(index: Int)
    }

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        val eventImage: ImageView = view.findViewById(R.id.eventImage)
        val eventName: TextView = view.findViewById(R.id.eventName)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val guestCount: TextView = view.findViewById(R.id.guest_count)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)

        override fun onClick(v:View?){
            callback.onEventClicked(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name
        holder.eventDate.text = event.date
        holder.eventImage.setImageBitmap(event.imageBitmap)
        holder.guestCount.text = event.numOfGuests.toString()
        holder.deleteButton.setOnClickListener {
            callback.onEventDeleted(holder.adapterPosition)
        }
    }


    override fun getItemCount(): Int = events.size


}

