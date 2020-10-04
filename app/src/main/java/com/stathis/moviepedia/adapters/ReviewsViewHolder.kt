package com.stathis.moviepedia.adapters

import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.databinding.ReviewsItemRowBinding
import com.stathis.moviepedia.models.Reviews

class ReviewsViewHolder(var binding: ReviewsItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(reviews:Reviews){
        binding.author.text = reviews.author
        binding.searchType.text = reviews.content
    }
}