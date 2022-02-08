package com.stathis.moviepedia.ui.genres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.abstraction.DiffUtilClassV2
import com.stathis.moviepedia.databinding.HolderMovieItemBigBinding
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.LocalModel

class MovieTypeAdapter(val callback : GenericCallback) : ListAdapter<LocalModel, MovieTypeViewHolder>(DiffUtilClassV2<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTypeViewHolder {
        val view = HolderMovieItemBigBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieTypeViewHolder(view,callback)
    }

    override fun onBindViewHolder(holder: MovieTypeViewHolder, position: Int) {
        holder.present(getItem(position))
    }
}