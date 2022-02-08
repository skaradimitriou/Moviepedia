package com.stathis.moviepedia.ui.movieDetails.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.BR
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.Reviews

class ReviewsViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun present(data : LocalModel){
        when(data){
            is Reviews -> binding.setVariable(BR.model,data)
        }
    }
}