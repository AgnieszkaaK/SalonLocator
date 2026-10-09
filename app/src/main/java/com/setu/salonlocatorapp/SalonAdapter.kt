package com.setu.salonlocatorapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.setu.salonlocatorapp.models.SalonModel

class SalonAdapter(
    private var salons: List<SalonModel>,
    private val onEdit: (SalonModel) -> Unit,
    private val onDelete: (SalonModel) -> Unit
) : RecyclerView.Adapter<SalonAdapter.SalonViewHolder>() {

    // Holds references to the controls in one salon row
    class SalonViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nameText: TextView =
            itemView.findViewById(R.id.nameText)

        val locationText: TextView =
            itemView.findViewById(R.id.locationText)

        val detailsText: TextView =
            itemView.findViewById(R.id.detailsText)

        val editButton: Button =
            itemView.findViewById(R.id.editButton)

        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SalonViewHolder {

        // Create a row using the XML layout
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.salon_item,
                parent,
                false
            )

        return SalonViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SalonViewHolder,
        position: Int
    ) {

        val salon = salons[position]

        holder.nameText.text =
            "${salon.id}: ${salon.name}"

        holder.locationText.text =
            salon.location

        holder.detailsText.text =
            "Price: €${salon.price}, Distance: ${salon.distanceKm} km"

        holder.editButton.setOnClickListener {
            onEdit(salon)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(salon)
        }
    }

    override fun getItemCount(): Int {
        return salons.size
    }

    fun updateSalons(newSalons: List<SalonModel>) {
        salons = newSalons
        notifyDataSetChanged()
    }
}