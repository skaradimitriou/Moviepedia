package com.stathis.moviepedia.adapters.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stathis.moviepedia.R
import com.stathis.moviepedia.abstraction.DiffUtilClassV2
import com.stathis.moviepedia.databinding.GenresItemRowBinding
import com.stathis.moviepedia.databinding.HolderEmptyLayoutBinding
import com.stathis.moviepedia.databinding.HolderShimmerGenresBinding
import com.stathis.moviepedia.listeners.GenericCallback
import com.stathis.moviepedia.models.EmptyModel
import com.stathis.moviepedia.models.LocalModel
import com.stathis.moviepedia.models.genres.MovieGenres

class GenresAdapter(val callback: GenericCallback) : ListAdapter<LocalModel, GenresViewHolder>(DiffUtilClassV2<LocalModel>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val view = when (viewType) {
            R.layout.genres_item_row -> GenresItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            R.layout.holder_shimmer_genres -> HolderShimmerGenresBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            else -> HolderEmptyLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        }
        return GenresViewHolder(view,callback)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.present(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is MovieGenres ->  R.layout.genres_item_row
        is EmptyModel -> R.layout.holder_shimmer_genres
        else ->R.layout.holder_empty_layout
    }
}