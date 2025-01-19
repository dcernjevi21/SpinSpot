package com.example.bassbytecreators.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bassbytecreators.R
import com.example.bassbytecreators.entities.Review

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewerName: TextView = view.findViewById(R.id.tvReviewerName)
        val reviewText: TextView = view.findViewById(R.id.tvReviewText)
        val reviewRating: TextView = view.findViewById(R.id.tvReviewRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.reviewerName.text = review.reviewer ?: "Nepoznato"
        holder.reviewText.text = review.opis
        holder.reviewRating.text = "⭐ ${review.rating}/5"
    }

    // Returns the total number of items
    override fun getItemCount(): Int {
        return reviews.size
    }
}
