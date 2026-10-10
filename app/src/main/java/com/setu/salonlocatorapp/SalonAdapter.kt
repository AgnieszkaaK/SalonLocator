package com.setu.salonlocatorapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.setu.salonlocatorapp.models.SalonModel
import timber.log.Timber
class SalonAdapter(
    private var salons: List<SalonModel>,
    private val listener: OnSalonListener
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

        // holds references to the personal rating and review labels
        val personalRatingText: TextView =
            itemView.findViewById(R.id.personalRatingText)

        val personalReviewText: TextView =
            itemView.findViewById(R.id.personReviewText)
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

        // displays the salon's personal rating
        holder.personalRatingText.text =
            if (salon.personalRating > 0) { //if above 0
                "Your rating: ${salon.personalRating}/5"
            } else {
                "Not rated yet"
            }

        // displays the review and hides the label if it is empty
        holder.personalReviewText.text = salon.personalReview
        holder.personalReviewText.visibility =
            if (salon.personalReview.isBlank()) {
                View.GONE
            } else {
                View.VISIBLE
            }

        holder.itemView.setOnClickListener {
            Timber.d("Salon tapped id=${salon.id}")
            listener.onSalonClick(salon)
        }

        holder.itemView.setOnLongClickListener {
            Timber.d("Salon long-pressed id=${salon.id}")
            listener.onSalonLongClick(salon)
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