package com.stathis.moviepedia.ui.movieDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.adapters.DiffUtilClass
import com.stathis.moviepedia.databinding.HolderReviewItemRowBinding
import com.stathis.moviepedia.models.LocalModel

class ReviewsAdapter : ListAdapter<LocalModel, ReviewsViewHolder>(DiffUtilClass<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view = HolderReviewItemRowBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.present(getItem(position))
    }
}