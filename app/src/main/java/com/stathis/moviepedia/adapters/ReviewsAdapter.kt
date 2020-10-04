package com.stathis.moviepedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.databinding.ReviewsItemRowBinding
import com.stathis.moviepedia.models.Reviews

class ReviewsAdapter : ListAdapter<Any, ReviewsViewHolder>(DiffUtilClass<Any>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view = ReviewsItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem as Reviews)
    }

}