package com.stathis.moviepedia.adapters

import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.databinding.ReviewsItemRowBinding
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.Reviews

class ReviewsViewHolder(var binding: ReviewsItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    fun present(localModel : LocalModel){
        when(localModel){
            is Reviews -> {
                binding.author.text = localModel.author
                binding.searchType.text = localModel.content
            }
        }
    }
}