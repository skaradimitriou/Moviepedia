package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.databinding.ReviewsItemRowBinding
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.Reviews

class ReviewsAdapter : ListAdapter<LocalModel, ReviewsViewHolder>(DiffUtilClass<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        return ReviewsViewHolder(
            ReviewsItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.present(getItem(position))
    }
}