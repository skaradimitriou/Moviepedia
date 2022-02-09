package com.stathis.moviepedia.ui.dashboard.profile.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.BR
import com.stathis.moviepedia.models.movies.Movies
import com.stathis.moviepedia.models.series.TvSeries

class FavoritesViewHolder(val binding : ViewDataBinding,val callback : GenericCallback) : RecyclerView.ViewHolder(binding.root) {

    fun present(data : LocalModel){
        when(data){
            is Movies -> {
                binding.setVariable(BR.model,data)
                binding.setVariable(BR.callback,callback)
            }
            is TvSeries -> {
                binding.setVariable(BR.model,data)
                binding.setVariable(BR.callback,callback)
            }
        }
    }
}