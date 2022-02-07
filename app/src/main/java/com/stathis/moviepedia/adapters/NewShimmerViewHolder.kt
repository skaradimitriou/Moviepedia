package com.stathis.moviepedia.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.models.LocalModel

class NewShimmerViewHolder(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun present(data: LocalModel){}
}