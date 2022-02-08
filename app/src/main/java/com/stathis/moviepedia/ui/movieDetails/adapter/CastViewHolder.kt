package com.stathis.moviepedia.ui.movieDetails.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.BR
import com.stathis.moviepedia.models.cast.Cast

class CastViewHolder(val binding : ViewDataBinding,val callback: GenericCallback) : RecyclerView.ViewHolder(binding.root) {

    fun present(data: LocalModel) {
        when (data) {
            is Cast -> {
                binding.setVariable(BR.model,data)
                binding.setVariable(BR.callback,callback)
            }
        }
    }
}