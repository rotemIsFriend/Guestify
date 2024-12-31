package com.example.guestify.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guestify.R
import com.example.guestify.data.model.Guest

class GuestAdapter(
    private var guests: List<Guest>,
    private val onEditClick: (Guest) -> Unit,
    private val onDeleteClick: (Guest) -> Unit
) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    inner class GuestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.guest_name)
        val phone: TextView = view.findViewById(R.id.guest_phone)
        val edit: ImageView = view.findViewById(R.id.edit_guest)
        val delete: ImageView = view.findViewById(R.id.delete_guest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.guest_item, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val guest = guests[position]
        holder.name.text = guest.name
        holder.phone.text = guest.phone
        holder.edit.setOnClickListener { onEditClick(guest) }
        holder.delete.setOnClickListener { onDeleteClick(guest) }
    }

    override fun getItemCount() = guests.size

    fun updateGuests(newGuests: List<Guest>) {
        guests = newGuests
        notifyDataSetChanged()
    }
}
